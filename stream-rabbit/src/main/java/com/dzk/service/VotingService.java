package com.dzk.service;

import com.dzk.bean.Vote;
import com.dzk.bean.VoteResult;
import org.springframework.stereotype.Service;

/**
 * Created by dzk on 2018/11/17.
 */
@Service
public class VotingService {

    public VoteResult record(Vote vote) {
        return new VoteResult();
    }
}
