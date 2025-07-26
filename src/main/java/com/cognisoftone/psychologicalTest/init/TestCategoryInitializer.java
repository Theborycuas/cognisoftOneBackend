package com.cognisoftone.psychologicalTest.init;

import com.cognisoftone.psychologicalTest.model.TestCategoryModel;
import com.cognisoftone.psychologicalTest.repository.TestCategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestCategoryInitializer {

    private final TestCategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        List<String> defaultCategories = List.of(
                "Ansiedad",
                "Depresión",
                "Estrés",
                "Personalidad",
                "Ansiedad Social"
        );

        for (String name : defaultCategories) {
            if (categoryRepository.findByName(name).isEmpty()) {
                TestCategoryModel category = new TestCategoryModel();
                category.setName(name);
                category.setActive(true);
                category.setCreatedAt(LocalDateTime.now());
                category.setUpdatedAt(LocalDateTime.now());

                categoryRepository.save(category);
            }
        }
    }
}


