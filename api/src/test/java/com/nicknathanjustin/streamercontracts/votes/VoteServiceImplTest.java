package com.nicknathanjustin.streamercontracts.votes;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
import com.nicknathanjustin.streamercontracts.contracts.ContractState;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import lombok.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VoteServiceImplTest {

    private static final UUID PROPOSER_ID = UUID.randomUUID();
    private static final UUID STREAMER_ID = UUID.randomUUID();
    // Subtract 10000ms from timestamp to address potential race conditions
    private static final Timestamp SETTLE_CONTRACT_TIMESTAMP = new Timestamp(System.currentTimeMillis() - 10000);

    @Mock private VoteModelRepository mockVoteModelRepository;
    @InjectMocks private VoteServiceImpl voteServiceImpl;

    @Test(expected = NullPointerException.class)
    public void recordVote_nullUserModel_throwsException() {
        voteServiceImpl.recordVote(null,
                createContractModel(
                        getValidContractModelBuilder(),
                        createUserModel(PROPOSER_ID),
                        createUserModel(STREAMER_ID)),
                true);
    }

    @Test(expected = NullPointerException.class)
    public void recordVote_nullContractModel_throwsException() {
        voteServiceImpl.recordVote(createUserModel(PROPOSER_ID), null, true);
    }

    @Test(expected = NullPointerException.class)
    public void isVotingComplete_nullContractModel_throwsException() {
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = null;

        voteServiceImpl.isVotingComplete(proposerVote, streamerVote, null);
    }

    @Test(expected = NullPointerException.class)
    public void getVoteOutcome_nullContractModel_throwsException() {
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = null;

        voteServiceImpl.getVoteOutcome( proposerVote, streamerVote, null);
    }

    @Test(expected = IllegalStateException.class)
    public void recordVote_contractIsOpen_throwsException() {
        voteServiceImpl.recordVote(createUserModel(PROPOSER_ID),
                createContractModel(getValidContractModelBuilder().state(ContractState.OPEN),
                        createUserModel(PROPOSER_ID),
                        createUserModel(STREAMER_ID)),
                true);
    }

    @Test(expected = IllegalStateException.class)
    public void recordVote_contractIsCompleted_throwsException() {
        voteServiceImpl.recordVote(createUserModel(PROPOSER_ID),
                createContractModel(
                        getValidContractModelBuilder().state(ContractState.COMPLETED),
                        createUserModel(PROPOSER_ID),
                        createUserModel(STREAMER_ID)),
                true);
    }

    @Test(expected = IllegalStateException.class)
    public void recordVote_contractIsPastSettlesTimestamp_throwsException() {
        voteServiceImpl.recordVote(createUserModel(PROPOSER_ID),
                createContractModel(
                        getValidContractModelBuilder().settlesAt(SETTLE_CONTRACT_TIMESTAMP),
                        createUserModel(PROPOSER_ID),
                        createUserModel(STREAMER_ID)),
                        true);
    }

    @Test(expected = IllegalStateException.class)
    public void isVotingComplete_contractIsCommunityContract_throwsException() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder().isCommunityContract(true),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID)
        );
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = null;

        voteServiceImpl.isVotingComplete(proposerVote, streamerVote, contractModel);
    }

    @Test(expected = IllegalStateException.class)
    public void isVotingComplete_contractIsOpen_throwsException() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder().state(ContractState.OPEN),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID)
        );
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = null;

        voteServiceImpl.isVotingComplete(proposerVote, streamerVote, contractModel);
    }

    @Test(expected = IllegalStateException.class)
    public void isVotingComplete_contractIsCompleted_throwsException() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder().state(ContractState.COMPLETED),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID)
        );
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = null;

        voteServiceImpl.isVotingComplete(proposerVote, streamerVote, contractModel);
    }

    @Test(expected = IllegalStateException.class)
    public void getVoteOutcome_contractIsCommunityContract_throwsException() {
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = null;

        voteServiceImpl.getVoteOutcome(proposerVote, streamerVote, createContractModel(
                getValidContractModelBuilder().isCommunityContract(true),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID))
        );
    }

    @Test(expected = IllegalStateException.class)
    public void getVoteOutcome_contractIsOpen_throwsException() {
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = null;

        voteServiceImpl.getVoteOutcome(proposerVote, streamerVote, createContractModel(
                getValidContractModelBuilder().state(ContractState.OPEN),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID))
        );
    }

    @Test(expected = IllegalStateException.class)
    public void getVoteOutcome_contractIsCompleted_throwsException() {
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = null;

        voteServiceImpl.getVoteOutcome(proposerVote, streamerVote, createContractModel(
                getValidContractModelBuilder().state(ContractState.COMPLETED),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID))
        );
    }

    @Test
    public void recordVote_voterIsProposerAndHasNotVotedBefore_voteRecorded() {
        final ArgumentCaptor<VoteModel> voteModelArgumentCaptor = ArgumentCaptor.forClass(VoteModel.class);
        final UserModel voter = createUserModel(PROPOSER_ID);
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                voter,
                createUserModel(STREAMER_ID));
        final boolean flaggedCompleted = true;

        voteServiceImpl.recordVote(voter, contractModel, flaggedCompleted);

        verify(mockVoteModelRepository).save(voteModelArgumentCaptor.capture());
        final VoteModel voteModel = voteModelArgumentCaptor.getValue();
        Assert.assertEquals(contractModel, voteModel.getContract());
        Assert.assertEquals(voter, voteModel.getVoter());
        Assert.assertNotNull(voteModel.getVotedAt());
        Assert.assertEquals(flaggedCompleted, voteModel.isViewerFlaggedComplete());
    }

    @Test
    public void recordVote_voterIsStreamerAndHasNotVotedBefore_voteRecorded() {
        final ArgumentCaptor<VoteModel> voteModelArgumentCaptor = ArgumentCaptor.forClass(VoteModel.class);
        final UserModel voter = createUserModel(STREAMER_ID);
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                voter);
        final boolean flaggedCompleted = true;

        voteServiceImpl.recordVote(voter, contractModel, flaggedCompleted);

        verify(mockVoteModelRepository).save(voteModelArgumentCaptor.capture());
        final VoteModel voteModel = voteModelArgumentCaptor.getValue();
        Assert.assertEquals(contractModel, voteModel.getContract());
        Assert.assertEquals(voter, voteModel.getVoter());
        Assert.assertNotNull(voteModel.getVotedAt());
        Assert.assertEquals(flaggedCompleted, voteModel.isViewerFlaggedComplete());
    }

    @Test(expected = IllegalStateException.class)
    public void recordVote_voterIsNotProposerOrStreamer_throwsException() {
        final UserModel voter = createUserModel(UUID.randomUUID());

        voteServiceImpl.recordVote(voter,
                createContractModel(
                        getValidContractModelBuilder(),
                        createUserModel(PROPOSER_ID),
                        createUserModel(STREAMER_ID)),
                true);
    }

    @Test(expected = IllegalStateException.class)
    public void recordVote_voterIsProposerAndHasVotedBefore_throwsException() {
        final UserModel voter = createUserModel(PROPOSER_ID);
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                voter,
                createUserModel(STREAMER_ID));
        when(mockVoteModelRepository.findByContractIdAndVoterId(contractModel.getId(), voter.getId())).thenReturn(Optional.of(VoteModel.builder().build()));

        voteServiceImpl.recordVote(voter, contractModel, true);
    }

    @Test(expected = IllegalStateException.class)
    public void recordVote_voterIsStreamerAndHasVotedBefore_throwsException() {
        final UserModel voter = createUserModel(STREAMER_ID);
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                voter);
        when(mockVoteModelRepository.findByContractIdAndVoterId(contractModel.getId(), voter.getId())).thenReturn(Optional.of(VoteModel.builder().build()));

        voteServiceImpl.recordVote(voter, contractModel, true);
    }

    @Test
    public void isVotingComplete_proposerAndStreamerHaveVoted_returnsTrue() {
        final UserModel proposer = createUserModel(PROPOSER_ID);
        final UserModel streamer = createUserModel(STREAMER_ID);
        final ContractModel contractModel = createContractModel(getValidContractModelBuilder(), proposer, streamer);
        final VoteModel proposerVote = VoteModel.builder().contract(contractModel).viewerFlaggedComplete(true).build();
        final VoteModel streamerVote = VoteModel.builder().contract(contractModel).viewerFlaggedComplete(true).build();

        final boolean isVotingComplete = voteServiceImpl.isVotingComplete(proposerVote, streamerVote, contractModel);

        Assert.assertTrue(isVotingComplete);
    }

    @Test
    public void isVotingComplete_proposerMarkedContractCompleted_returnsTrue() {
        final ContractModel contractModel = createContractModel(
            getValidContractModelBuilder(),
            createUserModel(PROPOSER_ID),
            createUserModel(STREAMER_ID));
        contractModel.setContractState(ContractState.ACCEPTED);
        final VoteModel proposerVote = VoteModel.builder().contract(contractModel).viewerFlaggedComplete(true).build();
        final VoteModel streamerVote = null;

        final boolean isVotingComplete = voteServiceImpl.isVotingComplete(proposerVote, streamerVote, contractModel);

        Assert.assertTrue(isVotingComplete);
    }

    @Test
    public void isVotingComplete_streamerMarkedContractFailed_returnsTrue() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = VoteModel.builder().contract(contractModel).viewerFlaggedComplete(false).build();

        final boolean isVotingComplete = voteServiceImpl.isVotingComplete(proposerVote, streamerVote, contractModel);

        Assert.assertTrue(isVotingComplete);
    }

    @Test
    public void isVotingComplete_contractHasExpired_returnsTrue() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder().settlesAt(SETTLE_CONTRACT_TIMESTAMP),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = null;

        final boolean isVotingComplete = voteServiceImpl.isVotingComplete(proposerVote, streamerVote, contractModel);

        Assert.assertTrue(isVotingComplete);
    }

    @Test
    public void isVotingComplete_NoVotesAndNotExpired_returnsFalse() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = null;

        final boolean isVotingComplete = voteServiceImpl.isVotingComplete(proposerVote, streamerVote, contractModel);

        Assert.assertFalse(isVotingComplete);
    }

    @Test(expected = IllegalStateException.class)
    public void getVoteOutcome_votingNotComplete_throwsException() {
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = null;
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));

        voteServiceImpl.getVoteOutcome(proposerVote, streamerVote, contractModel);
    }

    @Test
    public void getVoteOutcome_proposerMarkedContractCompleted_returnsCompleted() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        final VoteModel proposerVote = VoteModel.builder().contract(contractModel).viewerFlaggedComplete(true).build();
        final VoteModel streamerVote = null;

        final ContractState voteOutcome = voteServiceImpl.getVoteOutcome(proposerVote, streamerVote, contractModel);

        Assert.assertEquals(ContractState.COMPLETED, voteOutcome);
    }

    @Test
    public void getVoteOutcome_streamerMarkedContractFailed_returnsFailed() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = VoteModel.builder().contract(contractModel).viewerFlaggedComplete(false).build();

        final ContractState voteOutcome = voteServiceImpl.getVoteOutcome(proposerVote, streamerVote, contractModel);

        Assert.assertEquals(ContractState.FAILED, voteOutcome);
    }

    @Test
    public void getVoteOutcome_noVotesButContractWasAccepted_returnsCompleted() {
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = null;

        final ContractState voteOutcome = voteServiceImpl.getVoteOutcome(proposerVote, streamerVote, createContractModel(
                getValidContractModelBuilder().state(ContractState.ACCEPTED).settlesAt(SETTLE_CONTRACT_TIMESTAMP),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID))
        );

        Assert.assertEquals(ContractState.COMPLETED, voteOutcome);
    }

    // TODO: This test will need to change when the expiration logic has been implemented
    @Test(expected = IllegalStateException.class)
    public void getVoteOutcome_noVotesAndContractIsOpen_throwsException() {
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = null;

        voteServiceImpl.getVoteOutcome(proposerVote, streamerVote, createContractModel(
                getValidContractModelBuilder().state(ContractState.OPEN).settlesAt(SETTLE_CONTRACT_TIMESTAMP),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID))
        );
    }

    @Test
    public void getVoteOutcome_streamerDidNotVoteAndProposerMarkedFailed_returnsFailed() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder().settlesAt(SETTLE_CONTRACT_TIMESTAMP),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        final VoteModel proposerVote = VoteModel.builder().contract(contractModel).viewerFlaggedComplete(false).build();
        final VoteModel streamerVote = null;

        ContractState contractState = voteServiceImpl.getVoteOutcome(proposerVote, streamerVote, contractModel);

        Assert.assertEquals(contractState, ContractState.FAILED);
    }

    @Test
    public void getVoteOutcome_proposerDidNotVoteAndStreamerMarkedCompleted_returnsCompleted() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder().settlesAt(SETTLE_CONTRACT_TIMESTAMP),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        final VoteModel proposerVote = null;
        final VoteModel streamerVote = VoteModel.builder().contract(contractModel).viewerFlaggedComplete(true).build();

        final ContractState voteOutcome = voteServiceImpl.getVoteOutcome(proposerVote, streamerVote, contractModel);

        Assert.assertEquals(ContractState.COMPLETED, voteOutcome);
    }

    @Test
    public void getVoteOutcome_proposerAndStreamerDisagree_returnsDispute() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder().settlesAt(SETTLE_CONTRACT_TIMESTAMP),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        final VoteModel proposerVote = VoteModel.builder().contract(contractModel).viewerFlaggedComplete(false).build();
        final VoteModel streamerVote = VoteModel.builder().contract(contractModel).viewerFlaggedComplete(true).build();

        final ContractState voteOutcome = voteServiceImpl.getVoteOutcome(proposerVote, streamerVote, contractModel);

        Assert.assertEquals(ContractState.DISPUTED, voteOutcome);
    }

    private UserModel createUserModel(@NonNull final UUID userId) {
        return UserModel.builder()
                .id(userId)
                .build();
    }

    private ContractModel.ContractModelBuilder getValidContractModelBuilder() {
        final Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationTimestamp);
        calendar.add(Calendar.DAY_OF_WEEK, 3);
        final Timestamp settlesTimestamp = new Timestamp(calendar.getTime().getTime());

        return ContractModel.builder()
                .settlesAt(settlesTimestamp)
                .state(ContractState.ACCEPTED)
                .isCommunityContract(false);
    }

    private ContractModel createContractModel(@NonNull final ContractModel.ContractModelBuilder builder,
                                              @NonNull final UserModel proposer,
                                              @NonNull final UserModel streamer) {
        return builder
                .proposer(proposer)
                .streamer(streamer)
                .id(UUID.randomUUID())
                .build();
    }
}
