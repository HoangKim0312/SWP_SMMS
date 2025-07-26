package com.example.swp_smms.repository.impl;

import com.example.swp_smms.model.entity.*;
import com.example.swp_smms.model.entity.Class;
import com.example.swp_smms.model.payload.request.AccountFilterRequest;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.repository.CustomAccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomAccountRepositoryImpl implements CustomAccountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<AccountResponse> filterStudents(AccountFilterRequest filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Main query
        CriteriaQuery<AccountResponse> cq = cb.createQuery(AccountResponse.class);
        Root<Account> root = cq.from(Account.class);

        Join<Account, Role> roleJoin = root.join("role", JoinType.LEFT);
        Join<Account, Class> classJoin = root.join("clazz", JoinType.LEFT);
        Join<Account, MedicalProfile> mpJoin = root.join("medicalProfile", JoinType.LEFT);
        Join<MedicalProfile, StudentDisease> sd = mpJoin.join("diseases", JoinType.LEFT);
        Join<MedicalProfile, StudentAllergy> sa = mpJoin.join("allergies", JoinType.LEFT);
        Join<MedicalProfile, StudentCondition> sc = mpJoin.join("conditions", JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(filter, cb, root, roleJoin, classJoin, sd, sa, sc);

        cq.select(cb.construct(AccountResponse.class,
                root.get("accountId"),
                root.get("username"),
                root.get("fullName"),
                root.get("dob"),
                root.get("gender"),
                root.get("phone"),
                roleJoin.get("roleId"),
                root.get("email"),
                root.get("emailNotificationsEnabled"),
                root.get("notificationTypes")
        )).distinct(true).where(cb.and(predicates.toArray(new Predicate[0])));

        // Sorting
        if (Boolean.TRUE.equals(filter.getSortByDiseaseSeverity())) {
            cq.orderBy(cb.desc(sd.get("disease").get("severityLevel")));
        } else if (Boolean.TRUE.equals(filter.getSortByAllergySeverity())) {
            cq.orderBy(cb.desc(sa.get("severity")));
        }

        TypedQuery<AccountResponse> query = entityManager.createQuery(cq);
        query.setFirstResult(filter.getPage() * filter.getSize());
        query.setMaxResults(filter.getSize());

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Account> countRoot = countQuery.from(Account.class);
        Join<Account, Role> countRole = countRoot.join("role", JoinType.LEFT);
        Join<Account, Class> countClass = countRoot.join("clazz", JoinType.LEFT);
        Join<Account, MedicalProfile> countMp = countRoot.join("medicalProfile", JoinType.LEFT);
        Join<MedicalProfile, StudentDisease> countSd = countMp.join("diseases", JoinType.LEFT);
        Join<MedicalProfile, StudentAllergy> countSa = countMp.join("allergies", JoinType.LEFT);
        Join<MedicalProfile, StudentCondition> countSc = countMp.join("conditions", JoinType.LEFT);

        List<Predicate> countPredicates = buildPredicates(filter, cb, countRoot, countRole, countClass, countSd, countSa, countSc);
        countQuery.select(cb.countDistinct(countRoot)).where(cb.and(countPredicates.toArray(new Predicate[0])));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), PageRequest.of(filter.getPage(), filter.getSize()), total);
    }

    private List<Predicate> buildPredicates(AccountFilterRequest filter, CriteriaBuilder cb,
                                            Root<Account> root,
                                            Join<Account, Role> roleJoin,
                                            Join<Account, Class> classJoin,
                                            Join<MedicalProfile, StudentDisease> sd,
                                            Join<MedicalProfile, StudentAllergy> sa,
                                            Join<MedicalProfile, StudentCondition> sc) {

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(sd.get("active"), true));
        predicates.add(cb.equal(sa.get("active"), true));
        predicates.add(cb.equal(sc.get("active"), true));

        if (filter.getClassId() != null && filter.getClassId() != 0) {
            predicates.add(cb.equal(classJoin.get("classId"), filter.getClassId()));
        }

        if (filter.getGrade() != null && !filter.getGrade().isBlank()) {
            predicates.add(cb.equal(classJoin.get("grade"), filter.getGrade()));
        }

        if (filter.getRoleId() != null) {
            predicates.add(cb.equal(roleJoin.get("roleId"), filter.getRoleId()));
        }

        if (filter.getDiseaseId() != null) {
            predicates.add(cb.equal(sd.get("disease").get("diseaseId"), filter.getDiseaseId()));
        }

        if (filter.getIsChronic() != null) {
            predicates.add(cb.equal(sd.get("disease").get("chronic"), filter.getIsChronic()));
        }

        if (filter.getIsContagious() != null) {
            predicates.add(cb.equal(sd.get("disease").get("contagious"), filter.getIsContagious()));
        }

        if (filter.getAllergenId() != null) {
            predicates.add(cb.equal(sa.get("allergen").get("allergenId"), filter.getAllergenId()));
        }

        if (filter.getIsLifeThreatening() != null) {
            predicates.add(cb.equal(sa.get("isLifeThreatening"), filter.getIsLifeThreatening()));
        }

        if (filter.getConditionId() != null) {
            predicates.add(cb.equal(sc.get("condition").get("conditionId"), filter.getConditionId()));
        }

        return predicates;
    }
}
