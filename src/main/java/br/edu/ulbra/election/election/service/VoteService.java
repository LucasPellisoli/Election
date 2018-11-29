package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.client.CandidateClienteService;
import br.edu.ulbra.election.election.client.ElectionClientService;
import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
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
    private final ElectionClientService electionClientService;
    private final CandidateClienteService candidateClienteService;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_VOTER_NOT_FOUND = "Voter not found";

    @Autowired
    public VoteService(VoteRepository voteRepository, ModelMapper modelMapper, ElectionClientService electionClientService, CandidateClienteService candidateClienteService) {
        this.voteRepository = voteRepository;
        this.modelMapper = modelMapper;
        this.electionClientService = electionClientService;
        this.candidateClienteService = candidateClienteService;
    }

    public GenericOutput electionVote(VoteInput voteInput){
        validateInput(voteInput);
        Vote vote =  modelMapper.map(voteInput, Vote.class);

        if(vote.getCandidateId() != null){
            vote.setNullVote(false);
            try{
                CandidateOutput candidate = candidateClienteService.getById(vote.getCandidateId());
                vote.setBlankVote(false);
            }catch (GenericOutputException e){
                vote.setBlankVote(true);
            }
        }else{
            vote.setNullVote(true);
        }

        vote = voteRepository.save(vote);
        return  new GenericOutput("Vote feito");
    }

    public GenericOutput multipleElectionVote(List<VoteInput> voteInputList){
        for(VoteInput voteInput : voteInputList){
            validateInput(voteInput);
            voteRepository.save(modelMapper.map(voteInput, Vote.class));
        }
        return  new GenericOutput("Votos feito");
    }

    private void validateInput(VoteInput voteInput) {
        CandidateOutput candidate = candidateClienteService.getById(voteInput.getCandidateId());
        ElectionOutput election = electionClientService.getById(voteInput.getElectionId());

        if (election == null){
            throw new GenericOutputException("Error");
        }

        if(election.getId() != voteInput.getElectionId()){
            throw new GenericOutputException("Error getElectionId");
        }


        if(voteInput.getElectionId() == null){
            throw new GenericOutputException("Error");
        }

        if(voteInput.getVoterId() == null){
            throw new GenericOutputException("Error");
        }

    }

}
