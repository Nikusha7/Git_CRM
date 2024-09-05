package ge.nika.gym_crm.controllers;

import ge.nika.gym_crm.entities.Training;
import ge.nika.gym_crm.entities.TrainingTypeNames;
import ge.nika.gym_crm.services.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TrainingController {
    private static final Logger log = LoggerFactory.getLogger(TrainingController.class);

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping("/training/register")
    public ResponseEntity<?> registerTraining(@RequestParam String traineeUsername,
                                              @RequestParam String trainerUsername,
                                              @RequestParam String trainingName,
                                              @RequestParam Date trainingDate,
                                              @RequestParam Integer trainingDuration) {
        try {
            Training responseTraining = trainingService.create(traineeUsername, trainerUsername, trainingName, trainingDate, trainingDuration);
            return new ResponseEntity<>(responseTraining, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.error("Error registering training: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/training/get-trainings")
//    public ResponseEntity<?> getTrainingsByUsername(@RequestParam String username,
//                                                    @RequestParam(required = false) String periodFrom,
//                                                    @RequestParam(required = false) String periodTo,
//                                                    @RequestParam(required = false) String trainerName,
//                                                    @RequestParam(required = false) TrainingTypeNames trainingTypeNames) {
//        trainingService.select()
//    }

}
