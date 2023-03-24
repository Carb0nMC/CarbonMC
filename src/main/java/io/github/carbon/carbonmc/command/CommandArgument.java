package io.github.carbon.carbonmc.command;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandArgument {
    private int position;
    private ArrayList<String> values;

    public CommandArgument(int position, ArrayList<String> values) {
        this.position = position;
        this.values = values;
    }

    public CommandArgument(int position, String... values) {
        this.position = position;
        this.values = new ArrayList<>();
        this.values.addAll(Arrays.asList(values));
    }

    public int getPosition() {
        return position;
    }

    public ArrayList<String> getValues() {
        return values;
    }
}
