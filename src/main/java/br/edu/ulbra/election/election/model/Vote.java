package br.edu.ulbra.election.election.model;

import javax.persistence.*;

@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long electionId;

    @Column
    private Long voterId;

    @Column
    private Long candidateId;

    @Column
    private  boolean blankVote;

    @Column
    private  boolean NullVote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    public Long getVoterId() {
        return voterId;
    }

    public void setVoterId(Long voterId) {
        this.voterId = voterId;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public boolean isBlankVote() {
        return blankVote;
    }

    public void setBlankVote(boolean blankVote) {
        this.blankVote = blankVote;
    }

    public boolean isNullVote() {
        return NullVote;
    }

    public void setNullVote(boolean nullVote) {
        NullVote = nullVote;
    }
}
