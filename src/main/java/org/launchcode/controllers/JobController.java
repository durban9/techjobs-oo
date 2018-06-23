package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model,@RequestParam int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view
        Job job = jobData.findById(id);
        model.addAttribute("job", job);
        model.addAttribute("id", id);
        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add( @Valid JobForm jobForm, Model model, Errors errors) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.
        if (errors.hasErrors()){
            model.addAttribute(new JobForm());
            return"new-job";
        }
        String newName = jobForm.getName();

        int newEmployerId = jobForm.getEmployerId();
        Employer employer = jobData.getEmployers().findById(newEmployerId);

        int newLocationId = jobForm.getLocationId();
        Location location = jobData.getLocations().findById(newLocationId);

        int positionTypesId = jobForm.getPositionTypesId();
        PositionType positionType = jobData.getPositionTypes().findById(positionTypesId);

        int coreCompetenciesId = jobForm.getCoreCompetenciesId();
        CoreCompetency coreCompetency = jobData.getCoreCompetencies().findById(coreCompetenciesId);





        Job newJob = new Job(newName, employer, location, positionType, coreCompetency);
        jobData.add(newJob);
        int newId = newJob.getId();

        Job job = jobData.findById(newId);

        model.addAttribute("job", job);
        model.addAttribute("id", newId);

        return "redirect:?id="+newId;

    }
}
