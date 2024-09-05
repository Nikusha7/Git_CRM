package ge.nika.gym_crm.controllers;

import ge.nika.gym_crm.entities.TrainingType;
import ge.nika.gym_crm.entities.TrainingTypeNames;
import ge.nika.gym_crm.repositories.TrainingTypeRepository;
import ge.nika.gym_crm.services.TrainingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TrainingTypesController {
    private final TrainingTypeRepository trainingTypeRepository;


    public TrainingTypesController(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @GetMapping("/training-types")
    public List<TrainingType> getTrainingTypes(){
        return trainingTypeRepository.findAll();
    }


}
