package View;

public class GoogleMap {
	public static void drawMap(String command) {
		try {
			String[] yAndX = command.split(" ");
			int y = Integer.parseInt(yAndX[0]);
			int x = Integer.parseInt(yAndX[1]);
			String toPrint = "";
			for (int i = 0; i < 3; i++) {
				toPrint = "";
				for (int j = 0; j < 3; j++) {
					if (y == i && x == j) {
						toPrint += "* ";
						continue;
					}
					toPrint += ". ";
				}
				System.out.println(toPrint);
			}
		} catch (Exception e) {
		}
	}
}
