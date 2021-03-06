package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.ElectionInput;
import br.edu.ulbra.election.election.model.Election;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.repository.ElectionRepository;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ElectionService {

    private final ElectionRepository electionRepository;

    private final ModelMapper modelMapper;


    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_VOTER_NOT_FOUND = "Voter not found";
    private static final String STATES[]={"BR","AC","AL","AP","AM","BA","CE","DF","ES","GO","MA","MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN","RS","RO","RR","SC","SP","SE","TO"};
    @Autowired
    public ElectionService(ElectionRepository electionRepository, ModelMapper modelMapper) {
        this.electionRepository = electionRepository;
        this.modelMapper = modelMapper;
    }

    public List<ElectionOutput> getAll(){
        Type electionOutputListType = new TypeToken<List<ElectionOutput>>(){}.getType();
        return modelMapper.map(electionRepository.findAll(), electionOutputListType);
    }

    public List<ElectionOutput> getByYear(Integer year){
        Type electionOutputListType = new TypeToken<List<ElectionOutput>>(){}.getType();
        return modelMapper.map(electionRepository.findAllByYear(year), electionOutputListType);
    }

    public ElectionOutput getById(Long electionId){
        if (electionId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Election election = electionRepository.findById(electionId).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_VOTER_NOT_FOUND);
        }
        return modelMapper.map(election, ElectionOutput.class);
    }

    public ElectionOutput create(ElectionInput electionInput){
        validateInput(electionInput);
        electionRepository.save(modelMapper.map(electionInput, Election.class));
        return modelMapper.map(electionInput,ElectionOutput.class);
    }

    public ElectionOutput update(Long electionId, ElectionInput electionInput){
        validateInput(electionInput);
        if (electionId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Election election = electionRepository.findById(electionId).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_VOTER_NOT_FOUND);
        }

        election.setYear(electionInput.getYear());
        election.setStateCode(electionInput.getStateCode());
        election.setDescription(electionInput.getDescription());

        election = electionRepository.save(election);

        return modelMapper.map(election, ElectionOutput.class);
    }

    public GenericOutput delete(Long electionId){
        if (electionId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Election election = electionRepository.findById(electionId).orElse(null);
        if (election == null){
            throw new GenericOutputException(MESSAGE_VOTER_NOT_FOUND);
        }
        electionRepository.delete(election);

        return new GenericOutput("Deleted");
    }

    private void validateInput(ElectionInput electionInput){
        if(StringUtils.isBlank(electionInput.getStateCode())){
            throw new GenericOutputException("State is required!");
        }
        if(StringUtils.isBlank(electionInput.getDescription())){
            throw new GenericOutputException("Description is required!");
        }
        if(electionInput.getYear() == null){
            throw new GenericOutputException("Year is required!");
        }
        if(electionInput.getYear() <= 1999 && electionInput.getYear() >= 2200){
            throw new GenericOutputException("Invalid year range,, must be between 1999 and 2200");
        }
        List<String> listStates = Arrays.asList(STATES);
        if(!listStates.contains(electionInput.getStateCode())){
            throw new GenericOutputException("State not found");
        }
    }
}
