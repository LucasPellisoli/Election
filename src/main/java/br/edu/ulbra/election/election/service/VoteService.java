package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.repository.VoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VoteService {

    private final VoteRepository voteRepository;

    private final ModelMapper modelMapper;


    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_VOTER_NOT_FOUND = "Voter not found";

    @Autowired
    public VoteService(VoteRepository voteRepository, ModelMapper modelMapper) {
        this.voteRepository = voteRepository;
        this.modelMapper = modelMapper;
    }

    public GenericOutput electionVote(VoteInput voteInput){
        validateInput(voteInput);
        voteRepository.save(modelMapper.map(voteInput, Vote.class));
        return  new GenericOutput("Vote feito");
    }

    public GenericOutput multipleElectionVote(List<VoteInput> voteInputList){
        for(VoteInput voteInput : voteInputList){
            validateInput(voteInput);
            voteRepository.save(modelMapper.map(voteInput, Vote.class));
        }
        return  new GenericOutput("Votos feito");
    }

    private boolean isValid(Long id){
        Vote vote = voteRepository.findById(id).orElse(null);

        if(vote != null){
            return false;
        }
        return true;
    }

    private void validateInput(VoteInput voteInput) {

        if(voteInput.getCandidateId() == null){
            throw new GenericOutputException("Error");
        }

        if(voteInput.getElectionId() == null){
            throw new GenericOutputException("Error");
        }

        if(voteInput.getVoterId() == null){
            throw new GenericOutputException("Error");
        }

        if(isValid(voteInput.getVoterId())){
            throw new GenericOutputException("Error");
        }

    }

}
