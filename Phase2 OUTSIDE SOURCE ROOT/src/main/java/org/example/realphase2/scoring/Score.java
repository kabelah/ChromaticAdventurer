package org.example.realphase2.scoring;

/**
 * The Score class calculates and evaluates a players score depending
 * the time taken and the number of extra colours used. 
 * It generates a score between 0 and 100, with deductions for exceeding the optimal 
 * time limit and using more colors than necessary.
 */
public class Score {
    public int extraColoursUsed;
    public int timeTaken;
    public String convertedTimeTaken;
    public double score;

    /**
     * Constructs a Score object with the given number of extra colors and time taken.
     *
     * @param extraColoursUsedTemp The number of extra colours used by the player.
     * @param timeTakenTemp        The time taken by the player to complete the game, in seconds.
     */
    public Score(int extraColoursUsedTemp, int timeTakenTemp) {
        this.extraColoursUsed = extraColoursUsedTemp;
        this.timeTaken = timeTakenTemp;
    }

    /**
     * Calculates the player's score based on the time taken and extra colors used.
     * 
     * How Scoring works:
     * - If the time taken is within 60 seconds, the score is 100 minus 10 points per extra color.
     * - If the time taken exceeds 60 seconds, the score is further reduced by 0.1 points for each second over 60.
     * - The minimum score is 0.
     * 
     * @return The calculated score, rounded to one decimal place.
     */
    public double calculateScore() {
        if (timeTaken > 60) {
            score = 100 - ((timeTaken - 60) * 0.1) - (extraColoursUsed * 10);
        } else {
            score = 100 - (extraColoursUsed * 10);
        }

        if (score < 0) {
            score = 0;
        }
        score = Math.round(score * 10) / 10.0;
        return score;
    }

    /**
     * Converts the time taken from seconds to a human-readable format.
     * - If the time is 60 seconds or less, the output is "X seconds".
     * - If the time is greater than 60 seconds, the output is "X minute(s) and Y second(s)".
     * 
     * @return A string representation of the time taken.
     */
    public String timeTakenConverter() {
        if (timeTaken <= 60) {
            convertedTimeTaken = timeTaken + " seconds";
        } else {
            convertedTimeTaken = timeTaken / 60 + " minute(s) and " + (timeTaken % 60) + " second(s)";
        }
        return convertedTimeTaken;
    }

    /**
     * Provides feedback based on the player's score.
     * 
     * Feedback Categories:
     * - Score = 100: Perfect performance.
     * - 70 <= Score < 100: Great performance with minor room for improvement.
     * - 55 <= Score < 70: Acceptable performance but with significant room for improvement.
     * - Score < 55: Below average performance, encouragement to retry.
     * 
     * @return An integer representing the feedback category: 1 = Perfect, 2 = Great, 3 = Acceptable, 4 = Below Average.
     */
    public int scoreMessage() {
        if (score == 100) {
            System.out.println("You did everything perfect! Maybe try a more challenging graph.\nYour score: " + score + "\nTime taken: " + convertedTimeTaken + "\nExtra colours used: " + extraColoursUsed);
            return 1;
        } else if (score >= 70 && score < 100) {
            System.out.println("You did great! But there is still some room for improvement.\nYour score: " + score + "\nTime taken: " + convertedTimeTaken + "\nExtra colours used: " + extraColoursUsed);
            return 2;
        } else if (score >= 55 && score < 70) {
            System.out.println("Okay, not bad! But there is still plenty of room for improvement.\nYour score: " + score + "\nTime taken: " + convertedTimeTaken + "\nExtra colours used: " + extraColoursUsed);
            return 3;
        } else {
            System.out.println("Nice try, but I know you can do better! Try again if you like.\nYour score: " + score + "\nTime taken: " + convertedTimeTaken + "\nExtra colours used: " + extraColoursUsed);
            return 4;
        }
    }
}