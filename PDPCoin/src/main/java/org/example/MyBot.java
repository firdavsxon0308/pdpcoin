package org.example;

import org.example.modul.User;
import org.example.repository.UserRepository;
import org.example.service.ButtonService;
import org.example.service.UserServiceImpl;
import org.example.states.BotState;
import org.example.states.Role;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramLongPollingBot {
    public MyBot(String botToken) {
        super(botToken);
    }

    private final UserServiceImpl userService = new UserServiceImpl();
    private final ButtonService buttonService = new ButtonService();
    private UserRepository userRepository = new UserRepository();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            System.out.println(chatId);
            if (null == userRepository.getUserRole(chatId)) {
                userRepository.createUser(new User(chatId, BotState.START));
            }
            User user = userRepository.getUserById(chatId);
            if (text.equals("/start")){
                user.setBotState(BotState.START);
                userRepository.updateBotState(chatId,BotState.CHOOSE_TYPE);
            } else {
                if (user.getRole() == Role.ADMIN) {

                } else if (user.getRole() == Role.TEACHER) {

                } else if (user.getRole() == Role.STUDENT){

                } else {

                }
            }

        } else if (update.hasCallbackQuery()) {

        }
    }

    @Override
    public String getBotUsername() {
        return "PdpCoinBot";
    }
}
