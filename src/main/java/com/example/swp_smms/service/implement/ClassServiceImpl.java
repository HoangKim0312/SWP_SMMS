package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.Class;
import com.example.swp_smms.model.payload.request.ClassRequest;
import com.example.swp_smms.model.payload.response.ClassResponse;
import com.example.swp_smms.repository.ClassRepository;
import com.example.swp_smms.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;
    private final ModelMapper modelMapper;

    @Override
    public ClassResponse createClass(ClassRequest request) {
        if (classRepository.existsByClassName(request.getClassName())) {
            throw new RuntimeException("Class name already exists.");
        }

        Class clazz = modelMapper.map(request, Class.class);
        Class saved = classRepository.save(clazz);
        return modelMapper.map(saved, ClassResponse.class);
    }

    @Override
    public List<ClassResponse> getClassesByGradeInCurrentYear(String grade) {
        int currentYear = LocalDate.now().getYear();
        List<Class> classes = classRepository.findByGradeAndCurrentYear(grade, currentYear);
        return classes.stream()
                .map(clazz -> modelMapper.map(clazz, ClassResponse.class))
                .toList();
    }


}
