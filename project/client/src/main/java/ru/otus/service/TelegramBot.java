package ru.otus.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.otus.component.CreateRequest;
import ru.otus.component.RequestImpl;
import ru.otus.config.BotConfig;

@Component
//@AllArgsConstructor

public class TelegramBot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(TelegramBot.class);
    private final BotConfig botConfig;
    private final WebClient datastoreClient;


    public TelegramBot(BotConfig botConfig, WebClient datastoreClient) {
        this.botConfig = botConfig;
        this.datastoreClient = datastoreClient;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            var firstWordOfMessage = messageText.toLowerCase().split(" ")[0];
            CreateRequest.byCommand(firstWordOfMessage)
                    .execRequest(datastoreClient, update.getMessage())
                    .subscribe(stringValue -> sendMessage(chatId, stringValue.value()));
        }
    }



    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }

}
