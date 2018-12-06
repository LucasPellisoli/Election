package br.edu.ulbra.election.election.service;

import br.edu.ulbra.election.election.client.CandidateClienteService;
import br.edu.ulbra.election.election.client.ElectionClientService;
import br.edu.ulbra.election.election.client.VoterClienteService;
import br.edu.ulbra.election.election.exception.GenericOutputException;
import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.model.Vote;
import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import br.edu.ulbra.election.election.output.v1.ElectionOutput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.output.v1.VoterOutput;
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
    private final VoterClienteService voterClienteService;


    @Autowired
    public VoteService(VoteRepository voteRepository, ModelMapper modelMapper, ElectionClientService electionClientService, CandidateClienteService candidateClienteService, VoterClienteService voterClienteService) {
        this.voteRepository = voteRepository;
        this.modelMapper = modelMapper;
        this.electionClientService = electionClientService;
        this.candidateClienteService = candidateClienteService;
        this.voterClienteService = voterClienteService;
    }

    public GenericOutput electionVote(String token, VoteInput voteInput){
        validateInput(voteInput);
        Vote vote =  modelMapper.map(voteInput, Vote.class);

        VoterOutput voterValid = this.voterClienteService.checkToken(token);
        if(voterValid.getId() != voteInput.getVoterId()){
            throw new GenericOutputException("Invalid Voter");
        }
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

        voteRepository.save(vote);
        return  new GenericOutput("Made vote");
    }

    public GenericOutput multipleElectionVote(String token, List<VoteInput> voteInputList){
        for(VoteInput voteInput : voteInputList){
            validateInput(voteInput);
            voteRepository.save(modelMapper.map(voteInput, Vote.class));
        }
        return  new GenericOutput("Made vote");
    }

    private void validateInput(VoteInput voteInput) {
        ElectionOutput election = electionClientService.getById(voteInput.getElectionId());

        if (election == null){
            throw new GenericOutputException("Election not found!");
        }

        if(election.getId() != voteInput.getElectionId()){
            throw new GenericOutputException("Election ID is required!");
        }


        if(voteInput.getElectionId() == null){
            throw new GenericOutputException("Vote ID is required!");
        }

        if(voteInput.getVoterId() == null){
            throw new GenericOutputException("Volter ID is required!");
        }

    }

}
