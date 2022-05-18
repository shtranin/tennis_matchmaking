package com.kefx.tennis_matchmaking.commands.base;

import com.google.common.collect.ImmutableMap;
import com.kefx.tennis_matchmaking.commands.phaseCommand.TakingNameCommand;
import com.kefx.tennis_matchmaking.commands.fromButtons.*;
import com.kefx.tennis_matchmaking.commands.specific_commands.*;
import com.kefx.tennis_matchmaking.documents.UserStatementDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandContainer {

    private final ImmutableMap<String,Command> container;
    private final UnknownCommand unknownCommand;

    @Autowired
    public CommandContainer(UnknownCommand unknownCommand, RegistrationCommand registrationCommand,
                            ShowTableCommand showTableCommand, StartCommand startCommand,
                            TakingNameCommand takingNameCommand, MenuCommand menuCommand,
                            SettingsButtonCommand settingsButtonCommand, DeleteUserButton deleteUserButton,
                            FinallyDeleteUser finallyDeleteUser, RenameButton renameButton, InfoCommand infoCommand,
                            CreateGameButton createGameButton, RivalButton rivalButton, PlayerButton playerButton,
                            WinnerButton winnerButton, CancelGameResultButton cancelGameResultButton,AcceptGameResultButton acceptGameResultButton,
                            NothingCommand nothingCommand,MyStatisticButton myStatisticButton,RatingButton ratingButton,
                            GameDetailsCommand gameDetailsCommand) {

        this.unknownCommand = unknownCommand;
        container = ImmutableMap.<String, Command>builder()
                .put(CommandType.registration.getName(),registrationCommand)
                .put(CommandType.showTable.getName(), showTableCommand)
                .put(CommandType.start.getName(),startCommand)
                .put(CommandType.askingName.getName(), takingNameCommand)
                .put(CommandType.menu.getName(), menuCommand)
                .put(CommandType.settings.getName(),settingsButtonCommand)
                .put(CommandType.deleteUser.getName(), deleteUserButton)
                .put(CommandType.finallyDelete.getName(),finallyDeleteUser)
                .put(CommandType.rename.getName(),renameButton)
                .put(CommandType.info.getName(),infoCommand)
                .put(CommandType.createGame.getName(),createGameButton)
                .put(CommandType.rival.getName(),rivalButton)
                .put(CommandType.player.getName(),playerButton)
                .put(CommandType.winner.getName(),winnerButton)
                .put(CommandType.cancel.getName(), cancelGameResultButton)
                .put(CommandType.accept.getName(),acceptGameResultButton)
                .put(CommandType.nothing.getName(),nothingCommand)
                .put(CommandType.statistic.getName(),myStatisticButton)
                .put(CommandType.rating.getName(),ratingButton)
                .put(CommandType.gameDetails.getName(),gameDetailsCommand)
                .build();
    }

    public Command receiveCommand(String command){
        return container.getOrDefault(command,unknownCommand);
    }

    public Command receiveCommand(UserStatementDocument userStatementDocument){
        String keyForGettingCommand = userStatementDocument.getInProcessOf() + userStatementDocument.getPhaseOfProcess();
        return container.getOrDefault(keyForGettingCommand,unknownCommand);
    }
}
