import main.AuctionBot;
import java.io.*;

void main(String[] args) throws Exception {
    if (args == null || args.length != 1) {
        System.err.println("Expected exactly one argument: initial budget");
        return;
    }

    int initialBudget;
    try {
        initialBudget = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
        System.err.println("Invalid budget: " + args[0]);
        return;
    }

    if (initialBudget < 0) {
        System.err.println("Budget must be non-negative.");
        return;
    }

    AuctionBot bot = new AuctionBot(initialBudget);
    bot.run();
}
/*
     Categories:
     Music, Sports, Kids, DIY, Video Games, ASMR, Beauty, Cooking, Finance

     Video: Category, View count, Comment count

     Viewer: subscribed, age, gender, “interests”
     (they map to the same categories above)

     Format example
     video.viewCount=12345,video.category=Kids,
     video.commentCount=987,viewer.subscribed=Y,
     viewer.age=18-24,viewer.gender=F,
     viewer.interests=Video Games;Music

     Round trip example

    → ASMR
    ← video.category=Kids,video.viewCount=12345,video.commentCount=987,
            viewer.subscribed=Y,viewer.age=25-34,viewer.gender=F,
            viewer.interests=Video Games;Music
    → 5 51
    ← W 12
    ← video.category=Music,video.viewCount=804213,video.commentCount=4511,
            viewer.subscribed=N,viewer.age=18-24,viewer.gender=M,
            viewer.interests=Music;ASMR;Sports
    → 10 30
    ← L
    ... (98 more rounds) ...
    ← S 1289 199
    ← video.category=Sports,video.viewCount=...
    ... (continues) ...
*/
