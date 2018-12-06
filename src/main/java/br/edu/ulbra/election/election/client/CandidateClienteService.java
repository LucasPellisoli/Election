package br.edu.ulbra.election.election.client;

import br.edu.ulbra.election.election.output.v1.CandidateOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class CandidateClienteService {
    private final CandidateClient candidateClient;

    @Autowired
    public CandidateClienteService(CandidateClient candidateClient){
        this.candidateClient = candidateClient;
    }

    public CandidateOutput getById(Long id) throws RuntimeException{
        if(id == null){
            return null;
        }
        return this.candidateClient.getById(id);
    }

    @FeignClient(value="candidate-service", url="${url.candidate-service}")
    private interface CandidateClient {

        @GetMapping("/v1/candidate/{candidateId}")
        CandidateOutput getById(@PathVariable(name = "candidateId") Long candidateId);
    }
}
