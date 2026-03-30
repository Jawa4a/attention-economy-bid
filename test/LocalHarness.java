import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;



static BufferedWriter writer;
static BufferedReader reader;

void sendLine(String line) throws IOException {
    writer.write(line);
    writer.newLine();
    writer.flush();
}

String readLine() throws IOException {
    return reader.readLine();
}

long start;

void decisionTime() {
    System.out.println("Round response time: " + ((System.nanoTime() - start) / 1_000_000.0) + " ms");
}

void main() throws Exception {
    ProcessBuilder pb = new ProcessBuilder(
            "java",
            "-cp",
            "out/production/attention-economy-bid",
            "Main",
            "10000000" // 10_000_000
    );
    pb.redirectError(ProcessBuilder.Redirect.INHERIT);

    Process process = pb.start();



    writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
    reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

    String category = readLine();
    System.out.println("Chosen category: " + category);

    start = System.nanoTime();
    sendLine("video.category=Kids,video.viewCount=12345,video.commentCount=987,viewer.subscribed=Y,viewer.age=25-34,viewer.gender=F,viewer.interests=Video Games;Music");
    System.out.println("Bid 1: " + readLine());
    decisionTime();

    sendLine("W 12");

    start = System.nanoTime();
    sendLine("video.category=Music,video.viewCount=804213,video.commentCount=4511,viewer.subscribed=N,viewer.age=18-24,viewer.gender=M,viewer.interests=Music;ASMR;Sports");
    System.out.println("Bid 2: " + readLine());
    decisionTime();

    sendLine("L");

    start = System.nanoTime();
    sendLine("video.category=ASMR,video.viewCount=12345,video.commentCount=987,viewer.subscribed=Y,viewer.age=25-34,viewer.gender=F,viewer.interests=ASMR;Music");
    System.out.println("Bid 1: " + readLine());
    decisionTime();

    sendLine("L");

    // sendLine("S 1289 199");

    writer.close();

    int exitCode = process.waitFor();
    System.out.println("Bot exited with code: " + exitCode);
}