import it.unical.mat.embasp.base.Handler;

public class AIPlayer extends GenericPlayer {

    // Path to ASP File to be excecuted
    public static String encodingResource="encodings/prova";

    // ASP Handler
    public static Handler handler;


    public static void main(String[] args) { new AIPlayer().handleGameCycles(); }

    protected static void init() {


    }

    @Override
    protected void chooseAndPrintInitialPosition() {
        super.chooseAndPrintInitialPosition();
    }

    @Override
    protected void chooseAndPrintNextAction() {
        super.chooseAndPrintNextAction();
    }
}
