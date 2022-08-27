package bot.objects.items;

import bot.BotMain;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Books extends Item{

    public Books(long id,String emoji,String text,int cost,boolean shop) {
        this.id=id;
        this.emoji=emoji;
        this.text=text;
        this.cost=cost;
        this.shop=shop;
    }

    public void use(long id,long chat_id){
        new BotMain().sendMessageTo(String.valueOf(chat_id),"\uD83D\uDCDA Ты взял учебники, почитал по одному слову и забил..");
        SendSticker sendSticker = new SendSticker();
        sendSticker.setSticker(new InputFile("CAACAgIAAxkBAAHuK_FjB40m49ZFp_XxIyw3lpuWDr5WsQACiFMAAuCjggeYTAn0eI9sGCkE"));
        sendSticker.setChatId(String.valueOf(chat_id));
        try {
            new BotMain().execute(sendSticker);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
