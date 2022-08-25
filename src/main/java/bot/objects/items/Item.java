package bot.objects.items;

public class Item {
    public long id;
    public String emoji;
    public int cost;
    public String text;

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", emoji='" + emoji + '\'' +
                ", cost=" + cost +
                ", text='" + text + '\'' +
                '}';
    }
}
