package fr.unice.polytech.startingpoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestBuilding {
    Building eglise;

    @BeforeEach
    void setUp(){
        eglise = new Building(BuildingEnum.Eglise);
    }
        @Test
    void testToString(){
        assertEquals("Eglise, Cout : 2, Quartier : Religion, Construit : false", eglise.toString());
    }

}
