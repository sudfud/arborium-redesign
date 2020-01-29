package com.mygdx.arborium.game;

import java.util.ArrayList;

public class Farm {
  private int size;
  private boolean locked;
  private ArrayList<Plot> plots;

  public Farm(boolean locked) {
    plots = new ArrayList<>();
    size = 0;
    this.locked = locked;
  }

  public void addPlot(Plot plot) {
    plots.add(plot);
    plot.setFarm(this);
    size++;
  }
}
