package Project;

import java.util.Random;

interface Lifeline {
    void use(Question q) throws LifelineAlreadyUsedException;
    boolean isUsed();
}

abstract class BaseLifeline implements Lifeline {
    protected boolean used = false;

    @Override
    public boolean isUsed() {
        return used;
    }
}

class AudiencePoll extends BaseLifeline {
    @Override
    public void use(Question q) throws LifelineAlreadyUsedException {
        if (used) throw new LifelineAlreadyUsedException("Audience Poll already used!");
        System.out.println("Audience suggests option " + (q.getCorrectIndex() + 1));
        used = true;
    }
}

class FiftyFifty extends BaseLifeline {
    @Override
    public void use(Question q) throws LifelineAlreadyUsedException {
        if (used) throw new LifelineAlreadyUsedException("50/50 Lifeline already used!");
        Random rand = new Random();
        int incorrect;
        do {
            incorrect = rand.nextInt(4);
        } while (incorrect == q.getCorrectIndex());
        System.out.println("Remaining options: " + (q.getCorrectIndex() + 1) + " and " + (incorrect + 1));
        used = true;
    }
}

class SkipQuestion extends BaseLifeline {
    @Override
    public void use(Question q) throws LifelineAlreadyUsedException {
        if (used) throw new LifelineAlreadyUsedException("Skip Lifeline already used!");
        System.out.println("\u001B[32mQuestion skipped. Automatically marked correct!\u001B[0m");
        used = true;
    }
}

class LifelineAlreadyUsedException extends Exception {
    public LifelineAlreadyUsedException(String message) {
        super(message);
    }
}

