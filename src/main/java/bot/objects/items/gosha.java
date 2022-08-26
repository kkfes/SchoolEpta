package bot.objects.items;

import bot.BotMain;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class gosha extends Item{

    public gosha(long id, String emoji, String text, int cost) {
        this.id=id;
        this.emoji=emoji;
        this.text=text;
        this.cost=cost;
    }

    public void use(long id,long chat_id){
        new BotMain().sendMessageTo(String.valueOf(chat_id),"\uD83E\uDEB4Ты взял гошу");
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
