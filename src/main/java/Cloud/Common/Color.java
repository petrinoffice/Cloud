package Cloud.Common;

public enum Color {
    /**
     * Класс Color отвечает за расскраску выводимой информации в консоль
     */
    ANSI_RESET(" \u001B[0m "),
    ANSI_GREEN(" \u001B[32m "),
    ANSI_RED(" \u001B[31m "),
    ANSI_BLUE(" \u001B[34m "),
    ANSI_PURPLE(" \u001B[35m "),
    ANSI_CYAN(" \u001B[36m "),
    ANSI_WHITE(" \u001B[37m ");

    private String color;

    Color(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
