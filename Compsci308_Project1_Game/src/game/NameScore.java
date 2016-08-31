package game;

public class NameScore implements Comparable<NameScore> {
	
	private String name;
	private Integer score;
	
	public NameScore(String name, Integer score) {
		this.name = name;
		this.score = score;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getScore() {
		return score;
	}

	@Override
	public int compareTo(NameScore other) {
		return this.getScore() < other.getScore() ? -1 : this.getScore() > other.getScore() ? 1 : 0;
	}

}
