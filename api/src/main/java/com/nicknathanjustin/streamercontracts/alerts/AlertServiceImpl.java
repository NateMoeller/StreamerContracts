package com.nicknathanjustin.streamercontracts.alerts;

import com.google.common.hash.Hashing;
import com.nicknathanjustin.streamercontracts.contracts.dtos.Contract;
import com.nicknathanjustin.streamercontracts.users.UserModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    @NonNull final private SimpMessageSendingOperations messagingTemplate;

    @Override
    public void sendNotification(@NonNull final UserModel user, @NonNull final String title, @NonNull final String description) {
        final NotificationMessage message = new NotificationMessage(title, description);
        final String url = "/alert/" + user.getTwitchUsername();

        messagingTemplate.convertAndSend(url, message);
    }

    @Override
    public void sendStreamActivateAlert(@NonNull final UserModel user, @NonNull final Contract contract) {
        final AlertMessage message = new AlertMessage(contract.getDescription(), contract.getContractAmount(), contract.getProposerName(), AlertType.ACTIVATE);
        sendStreamAlert(user, contract, message);
    }

    @Override
    public void sendStreamDeactivateAlert(@NonNull final UserModel user, @NonNull final Contract contract) {
        final AlertMessage message = new AlertMessage(contract.getDescription(), contract.getContractAmount(), contract.getProposerName(), AlertType.DEACTIVATE);
        sendStreamAlert(user, contract, message);
    }

    private void sendStreamAlert(@NonNull final UserModel user, @NonNull final Contract contract, @NonNull  final AlertMessage message) {
        final String alertChannelId = Hashing.sha256()
                .hashString(user.getId().toString(), StandardCharsets.UTF_8)
                .toString();
        final String url = "/alert/" + alertChannelId;

        messagingTemplate.convertAndSend(url, message);
    }
}
