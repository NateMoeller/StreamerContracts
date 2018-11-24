package com.nicknathanjustin.streamercontracts.votes;

import com.nicknathanjustin.streamercontracts.contracts.ContractModel;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VoteServiceImplTest {

    private static final UUID PROPOSER_ID = UUID.randomUUID();
    private static final UUID STREAMER_ID = UUID.randomUUID();
    //subtract 1000 from timestamp to address potential race conditions
    private static final Timestamp EXPIRED_CONTRACT_TIME_STAMP = new Timestamp(System.currentTimeMillis() - 1000);

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
        voteServiceImpl.isVotingComplete(null);
    }

    @Test(expected = NullPointerException.class)
    public void getVoteOutcome_nullContractModel_throwsException() {
        voteServiceImpl.getVoteOutcome(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void recordVote_contractIsCommunityContract_throwsException() {
        voteServiceImpl.recordVote(createUserModel(PROPOSER_ID),
                createContractModel(
                        getValidContractModelBuilder().isCommunityContract(true),
                        createUserModel(PROPOSER_ID),
                        createUserModel(STREAMER_ID)),
                true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void recordVote_contractNotAccepted_throwsException() {
        voteServiceImpl.recordVote(createUserModel(PROPOSER_ID),
                createContractModel(getValidContractModelBuilder().isAccepted(false),
                        createUserModel(PROPOSER_ID),
                        createUserModel(STREAMER_ID)),
                true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void recordVote_contractIsCompleted_throwsException() {
        voteServiceImpl.recordVote(createUserModel(PROPOSER_ID),
                createContractModel(
                        getValidContractModelBuilder().isCompleted(true),
                        createUserModel(PROPOSER_ID),
                        createUserModel(STREAMER_ID)),
                true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void isVotingComplete_contractIsCommunityContract_throwsException() {
        voteServiceImpl.isVotingComplete(createContractModel(
                getValidContractModelBuilder().isCommunityContract(true),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void isVotingComplete_contractNotAccepted_throwsException() {
        voteServiceImpl.isVotingComplete(createContractModel(
                getValidContractModelBuilder().isAccepted(false),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void isVotingComplete_contractIsCompleted_throwsException() {
        voteServiceImpl.isVotingComplete(createContractModel(
                getValidContractModelBuilder().isCompleted(true),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void getVoteOutcome_contractIsCommunityContract_throwsException() {
        voteServiceImpl.getVoteOutcome(createContractModel(
                getValidContractModelBuilder().isCommunityContract(true),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void getVoteOutcome_contractNotAccepted_throwsException() {
        voteServiceImpl.getVoteOutcome(createContractModel(
                getValidContractModelBuilder().isAccepted(false),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void getVoteOutcome_contractIsCompleted_throwsException() {
        voteServiceImpl.getVoteOutcome(createContractModel(
                getValidContractModelBuilder().isCompleted(true),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID))
        );
    }

    @Test
    public void recordVote_voterIsProposerAndHasntVotedBefore_voteRecorded() {
        final ArgumentCaptor<VoteModel> voteModelArgumentCaptor = ArgumentCaptor.forClass(VoteModel.class);
        final UserModel proposer = createUserModel(PROPOSER_ID);
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                proposer,
                createUserModel(STREAMER_ID));
        final boolean flaggedCompleted = true;

        voteServiceImpl.recordVote(proposer, contractModel, flaggedCompleted);

        verify(mockVoteModelRepository).save(voteModelArgumentCaptor.capture());
        final VoteModel voteModel = voteModelArgumentCaptor.getValue();
        Assert.assertEquals(contractModel, voteModel.getContract());
        Assert.assertEquals(proposer, voteModel.getVoter());
        Assert.assertNotNull(voteModel.getVotedAt());
        Assert.assertEquals(flaggedCompleted, voteModel.isViewerFlaggedComplete());
    }

    @Test
    public void recordVote_voterIsStreamerAndHasntVotedBefore_voteRecorded() {
        final ArgumentCaptor<VoteModel> voteModelArgumentCaptor = ArgumentCaptor.forClass(VoteModel.class);
        final UserModel streamer = createUserModel(STREAMER_ID);
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                streamer);
        final boolean flaggedCompleted = true;

        voteServiceImpl.recordVote(streamer, contractModel, flaggedCompleted);

        verify(mockVoteModelRepository).save(voteModelArgumentCaptor.capture());
        final VoteModel voteModel = voteModelArgumentCaptor.getValue();
        Assert.assertEquals(contractModel, voteModel.getContract());
        Assert.assertEquals(streamer, voteModel.getVoter());
        Assert.assertNotNull(voteModel.getVotedAt());
        Assert.assertEquals(flaggedCompleted, voteModel.isViewerFlaggedComplete());
    }

    @Test
    public void recordVote_voterIsNotProposerOrStreamer_noVoteRecorded() {
        voteServiceImpl.recordVote(createUserModel(UUID.randomUUID()),
                createContractModel(
                        getValidContractModelBuilder(),
                        createUserModel(PROPOSER_ID),
                        createUserModel(STREAMER_ID)),
                true);

        verify(mockVoteModelRepository, never()).save(any());
    }

    @Test
    public void recordVote_voterIsProposerAndHasVotedBefore_noVoteRecorded() {
        final UserModel proposer = createUserModel(PROPOSER_ID);
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                proposer,
                createUserModel(STREAMER_ID));
        when(mockVoteModelRepository.findByContractIdAndVoterId(contractModel.getId(), proposer.getId())).thenReturn(Optional.of(VoteModel.builder().build()));

        voteServiceImpl.recordVote(proposer, contractModel, true);

        verify(mockVoteModelRepository, never()).save(any());
    }

    @Test
    public void recordVote_voterIsStreamerAndHasVotedBefore_noVoteRecorded() {
        final UserModel streamer = createUserModel(STREAMER_ID);
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                streamer);
        when(mockVoteModelRepository.findByContractIdAndVoterId(contractModel.getId(), streamer.getId())).thenReturn(Optional.of(VoteModel.builder().build()));

        voteServiceImpl.recordVote(streamer, contractModel, true);

        verify(mockVoteModelRepository, never()).save(any());
    }

    @Test
    public void isVotingComplete_proposerAndStreamerHaveVoted_returnsTrue() {
        final UserModel proposer = createUserModel(PROPOSER_ID);
        final UserModel streamer = createUserModel(STREAMER_ID);
        final ContractModel contractModel = createContractModel(getValidContractModelBuilder(), proposer, streamer);
        when(mockVoteModelRepository.findByContractIdAndVoterId(contractModel.getId(), proposer.getId())).thenReturn(Optional.of(VoteModel.builder().build()));
        when(mockVoteModelRepository.findByContractIdAndVoterId(contractModel.getId(), streamer.getId())).thenReturn(Optional.of(VoteModel.builder().build()));

        final boolean isVotingComplete = voteServiceImpl.isVotingComplete(contractModel);

        Assert.assertTrue(isVotingComplete);
    }

    @Test
    public void isVotingComplete_proposerMarkedContractCompleted_returnsTrue() {
        final ContractModel contractModel = createContractModel(
            getValidContractModelBuilder(),
            createUserModel(PROPOSER_ID),
            createUserModel(STREAMER_ID));
        setUpProposerVote(contractModel, true);

        final boolean isVotingComplete = voteServiceImpl.isVotingComplete(contractModel);

        Assert.assertTrue(isVotingComplete);
    }

    @Test
    public void isVotingComplete_streamerMarkedContractFailed_returnsTrue() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        setUpStreamerVote(contractModel, false);

        final boolean isVotingComplete = voteServiceImpl.isVotingComplete(contractModel);

        Assert.assertTrue(isVotingComplete);
    }

    @Test
    public void isVotingComplete_contractHasExpired_returnsTrue() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder().expiresAt(EXPIRED_CONTRACT_TIME_STAMP),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));

        final boolean isVotingComplete = voteServiceImpl.isVotingComplete(contractModel);

        Assert.assertTrue(isVotingComplete);
    }

    @Test
    public void isVotingComplete_NoVotesAndNotExpired_returnsFalse() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));

        final boolean isVotingComplete = voteServiceImpl.isVotingComplete(contractModel);

        Assert.assertFalse(isVotingComplete);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getVoteOutcome_votingNotComplete_throwsException() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));

        voteServiceImpl.getVoteOutcome(contractModel);
    }

    @Test
    public void getVoteOutcome_proposerMarkedContractCompleted_returnsCompleted() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        setUpProposerVote(contractModel, true);

        final VoteOutcome voteOutcome = voteServiceImpl.getVoteOutcome(contractModel);

        Assert.assertEquals(VoteOutcome.COMPLETED, voteOutcome);
    }

    @Test
    public void getVoteOutcome_streamerMarkedContractFailed_returnsFailed() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder(),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        setUpStreamerVote(contractModel, false);

        final VoteOutcome voteOutcome = voteServiceImpl.getVoteOutcome(contractModel);

        Assert.assertEquals(VoteOutcome.FAILED, voteOutcome);
    }

    @Test
    public void getVoteOutcome_noVotesButContractWasAccepted_returnsCompleted() {
        final VoteOutcome voteOutcome = voteServiceImpl.getVoteOutcome(createContractModel(
                getValidContractModelBuilder().expiresAt(EXPIRED_CONTRACT_TIME_STAMP),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID))
        );

        Assert.assertEquals(VoteOutcome.COMPLETED, voteOutcome);
    }

    @Test
    public void getVoteOutcome_noVotesAndContractWasNotAccepted_returnsFailed() {
        final VoteOutcome voteOutcome = voteServiceImpl.getVoteOutcome(createContractModel(
                getValidContractModelBuilder().isAccepted(false).expiresAt(EXPIRED_CONTRACT_TIME_STAMP),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID))
        );

        Assert.assertEquals(VoteOutcome.FAILED, voteOutcome);
    }

    @Test
    public void getVoteOutcome_streamerDidNotVoteAndProposerDidNotMarkComplete_returnsFailed() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder().expiresAt(EXPIRED_CONTRACT_TIME_STAMP),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        setUpProposerVote(contractModel, false);

        final VoteOutcome voteOutcome = voteServiceImpl.getVoteOutcome(contractModel);

        Assert.assertEquals(VoteOutcome.FAILED, voteOutcome);
    }

    @Test
    public void getVoteOutcome_proposerDidNotVoteAndStreamerDidNotMarkFailed_returnsCompleted() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder().expiresAt(EXPIRED_CONTRACT_TIME_STAMP),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        setUpStreamerVote(contractModel, true);

        final VoteOutcome voteOutcome = voteServiceImpl.getVoteOutcome(contractModel);

        Assert.assertEquals(VoteOutcome.COMPLETED, voteOutcome);
    }

    @Test
    public void getVoteOutcome_proposerAndStreamerDisagree_returnsDispute() {
        final ContractModel contractModel = createContractModel(
                getValidContractModelBuilder().expiresAt(EXPIRED_CONTRACT_TIME_STAMP),
                createUserModel(PROPOSER_ID),
                createUserModel(STREAMER_ID));
        setUpStreamerVote(contractModel, true);
        setUpProposerVote(contractModel, false);

        final VoteOutcome voteOutcome = voteServiceImpl.getVoteOutcome(contractModel);

        Assert.assertEquals(VoteOutcome.DISPUTE, voteOutcome);
    }

    private UserModel createUserModel(@NonNull final UUID userId) {
        return UserModel.builder()
                .id(userId)
                .twitchUsername("SOME_VALUE")
                .build();
    }

    private ContractModel.ContractModelBuilder getValidContractModelBuilder() {
        final Timestamp creationTimestamp = new Timestamp(System.currentTimeMillis());
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationTimestamp);
        calendar.add(Calendar.DAY_OF_WEEK, 3);
        final Timestamp expiresTimestamp = new Timestamp(calendar.getTime().getTime());

        return ContractModel.builder()
                .expiresAt(expiresTimestamp)
                .isCompleted(null)
                .isCommunityContract(false)
                .isAccepted(true);
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

    private void setUpProposerVote(@NonNull final ContractModel contractModel, final boolean flaggedCompleted) {
        when(mockVoteModelRepository.findByContractIdAndVoterId(contractModel.getId(), PROPOSER_ID))
                .thenReturn(Optional.of(VoteModel.builder().viewerFlaggedComplete(flaggedCompleted).build()));
    }

    private void setUpStreamerVote(@NonNull final ContractModel contractModel, final boolean flaggedCompleted) {
        when(mockVoteModelRepository.findByContractIdAndVoterId(contractModel.getId(), STREAMER_ID))
                .thenReturn(Optional.of(VoteModel.builder().viewerFlaggedComplete(flaggedCompleted).build()));
    }
}
