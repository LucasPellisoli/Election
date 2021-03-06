package br.edu.ulbra.election.election.api.v1;

import br.edu.ulbra.election.election.input.v1.VoteInput;
import br.edu.ulbra.election.election.output.v1.GenericOutput;
import br.edu.ulbra.election.election.service.VoteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/vote")
public class VoteApi {

    private final VoteService voteService;

    public VoteApi(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/{voteInput}")
    public GenericOutput electionVote(@RequestHeader(value = "x-token") String token, @RequestBody VoteInput voteInput){
        return voteService.electionVote(token, voteInput);
    }

    @PutMapping("/multiple")
    public GenericOutput multipleElectionVote(@RequestHeader(value = "x-token") String token,@RequestBody List<VoteInput> voteInputList){
        return voteService.multipleElectionVote(token, voteInputList);
    }
}
