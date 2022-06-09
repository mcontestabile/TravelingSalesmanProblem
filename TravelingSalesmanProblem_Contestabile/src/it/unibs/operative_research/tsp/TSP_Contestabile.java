package it.unibs.operative_research.tsp;


import java.util.ArrayList;

import gurobi.*;

public class TSP_Contestabile {
    /*
     * Sfrutto la matrice delle adiacenze per rappresentare i costi del
     * grafo G(A,V). La dimensione della matrice è VxV, in quanto devo
     * rappresentare sia il costo i->j che quello j->i che, da consegna,
     * sono definiti come equivalenti.
     */

    public static final int[][] costs =
            {              /* 00  01 02 03 04  05 06  07  08 09  10  11  12   13   14 15   16 17  18 19  20  21  22  23  24  25  26 27  28   29  30 31   32 33  34  35  36  37 38  39 40  41  42  43 44  45*/
                    /* 00 */ {0, 5, 4, 10, 7, 9, 6, 7, 8, 7, 2, 5, 6, 7, 8, 9, 10, 4, 7, 4, 5, 6, 4, 5, 2, 8, 7, 2, 5, 10, 9, 5, 7, 8, 6, 8, 9, 5, 6, 7, 7, 4, 5, 8, 3, 4},
                    /* 01 */ {5, 0, 6, 8, 6, 4, 3, 7, 4, 2, 6, 4, 6, 9, 6, 5, 8, 5, 5, 6, 8, 6, 5, 5, 7, 5, 5, 7, 7, 8, 8, 5, 8, 5, 8, 7, 6, 7, 3, 5, 3, 6, 4, 8, 2, 8},
                    /* 02 */ {4, 6, 0, 8, 9, 8, 6, 9, 4, 6, 5, 7, 6, 7, 8, 7, 6, 8, 4, 7, 4, 9, 6, 2, 5, 8, 4, 2, 6, 6, 6, 4, 4, 6, 6, 6, 8, 2, 4, 6, 8, 7, 3, 9, 5, 7},
                    /* 03 */ {10, 8, 8, 0, 8, 8, 11, 10, 4, 6, 9, 10, 7, 3, 6, 5, 9, 11, 6, 11, 6, 8, 11, 10, 11, 5, 9, 10, 8, 8, 7, 11, 9, 7, 6, 8, 6, 8, 9, 9, 7, 6, 7, 9, 9, 8},
                    /* 04 */ {7, 6, 9, 8, 0, 2, 5, 6, 6, 4, 6, 8, 8, 5, 7, 5, 10, 7, 7, 3, 8, 6, 8, 8, 5, 6, 5, 9, 8, 9, 8, 7, 6, 8, 8, 10, 8, 7, 6, 7, 7, 8, 9, 9, 8, 4},
                    /* 05 */ {9, 4, 8, 8, 2, 0, 3, 7, 4, 2, 8, 6, 6, 7, 9, 7, 8, 5, 5, 5, 8, 4, 8, 9, 7, 8, 4, 10, 8, 8, 6, 5, 8, 9, 9, 8, 8, 6, 7, 6, 7, 6, 7, 7, 6, 6},
                    /* 06 */ {6, 3, 6, 11, 5, 3, 0, 7, 7, 5, 7, 7, 9, 8, 9, 8, 9, 2, 6, 7, 5, 7, 6, 6, 8, 8, 5, 8, 9, 8, 8, 2, 6, 6, 9, 5, 9, 7, 4, 7, 6, 7, 7, 9, 5, 5},
                    /* 07 */ {7, 7, 9, 10, 6, 7, 7, 0, 7, 5, 9, 9, 9, 11, 6, 9, 10, 5, 7, 3, 9, 11, 7, 7, 5, 5, 8, 9, 10, 8, 9, 9, 9, 3, 8, 6, 10, 7, 5, 8, 7, 8, 10, 5, 9, 8},
                    /* 08 */ {8, 4, 4, 4, 6, 4, 7, 7, 0, 2, 7, 6, 3, 7, 8, 7, 8, 9, 5, 9, 6, 8, 8, 6, 7, 7, 5, 6, 6, 10, 5, 8, 6, 9, 7, 10, 4, 4, 7, 7, 5, 6, 3, 9, 5, 8},
                    /* 09 */ {7, 2, 6, 6, 4, 2, 5, 5, 2, 0, 8, 6, 4, 7, 8, 7, 6, 7, 3, 7, 7, 6, 7, 7, 8, 7, 3, 8, 7, 10, 7, 7, 7, 7, 9, 9, 6, 5, 5, 5, 5, 4, 5, 9, 4, 6},
                    /* 10 */ {2, 6, 5, 9, 6, 8, 7, 9, 7, 8, 0, 4, 8, 6, 7, 8, 8, 5, 5, 6, 3, 6, 2, 6, 4, 7, 5, 4, 6, 10, 10, 7, 5, 6, 7, 8, 9, 3, 4, 7, 9, 6, 7, 7, 5, 2},
                    /* 11 */ {5, 4, 7, 10, 8, 6, 7, 9, 6, 6, 4, 0, 9, 7, 9, 9, 7, 9, 4, 7, 7, 2, 2, 6, 7, 8, 8, 7, 7, 11, 8, 9, 9, 6, 8, 7, 10, 7, 4, 6, 7, 7, 4, 9, 2, 6},
                    /* 12 */ {6, 6, 6, 7, 8, 6, 9, 9, 3, 4, 8, 9, 0, 6, 9, 8, 10, 10, 7, 6, 9, 10, 8, 4, 4, 4, 7, 8, 4, 7, 8, 10, 9, 6, 6, 12, 3, 7, 6, 6, 6, 8, 6, 8, 8, 8},
                    /* 13 */ {7, 9, 7, 3, 5, 7, 8, 11, 7, 7, 6, 7, 6, 0, 7, 8, 11, 8, 8, 8, 3, 5, 8, 8, 9, 7, 7, 7, 5, 10, 6, 10, 6, 8, 3, 8, 3, 5, 6, 7, 6, 3, 7, 6, 9, 5},
                    /* 14 */ {8, 6, 8, 6, 7, 9, 9, 6, 8, 8, 7, 9, 9, 7, 0, 5, 8, 9, 9, 9, 4, 8, 9, 10, 10, 5, 6, 9, 6, 8, 8, 8, 8, 7, 5, 9, 6, 6, 9, 4, 3, 7, 9, 8, 8, 8},
                    /* 15 */ {9, 5, 7, 5, 5, 7, 8, 9, 7, 7, 8, 9, 8, 8, 5, 0, 8, 8, 8, 8, 7, 8, 10, 9, 10, 4, 4, 9, 5, 7, 9, 10, 7, 6, 7, 12, 5, 5, 8, 4, 2, 6, 8, 5, 7, 9},
                    /* 16 */ {10, 8, 6, 9, 10, 8, 9, 10, 8, 6, 8, 7, 10, 11, 8, 8, 0, 9, 3, 9, 10, 8, 9, 8, 11, 8, 6, 8, 6, 11, 7, 10, 10, 7, 8, 7, 9, 8, 9, 4, 6, 10, 9, 10, 9, 6},
                    /* 17 */ {4, 5, 8, 11, 7, 5, 2, 5, 9, 7, 5, 9, 10, 8, 9, 8, 9, 0, 6, 8, 7, 9, 7, 8, 6, 8, 4, 6, 7, 6, 9, 4, 8, 8, 8, 7, 9, 6, 6, 6, 6, 8, 8, 8, 7, 3},
                    /* 18 */ {7, 5, 4, 6, 7, 5, 6, 7, 5, 3, 5, 4, 7, 8, 9, 8, 3, 6, 0, 9, 8, 6, 6, 6, 9, 6, 6, 6, 7, 9, 10, 8, 8, 4, 9, 10, 9, 6, 6, 7, 6, 7, 7, 7, 6, 3},
                    /* 19 */ {4, 6, 7, 11, 3, 5, 7, 3, 9, 7, 6, 7, 6, 8, 9, 8, 9, 8, 9, 0, 9, 9, 5, 5, 2, 7, 7, 6, 7, 10, 9, 8, 9, 5, 9, 9, 9, 9, 3, 5, 7, 6, 9, 8, 7, 7},
                    /* 20 */ {5, 8, 4, 6, 8, 8, 5, 9, 6, 7, 3, 7, 9, 3, 4, 7, 10, 7, 8, 9, 0, 8, 5, 6, 7, 9, 4, 6, 6, 7, 7, 7, 4, 8, 4, 5, 6, 2, 6, 6, 7, 6, 6, 7, 8, 5},
                    /* 21 */ {6, 6, 9, 8, 6, 4, 7, 11, 8, 6, 6, 2, 10, 5, 8, 8, 8, 9, 6, 9, 8, 0, 4, 8, 8, 8, 6, 8, 6, 11, 10, 9, 10, 8, 8, 9, 8, 8, 6, 4, 6, 8, 6, 11, 4, 8},
                    /* 22 */ {4, 5, 6, 11, 8, 8, 6, 7, 8, 7, 2, 2, 8, 8, 9, 10, 9, 7, 6, 5, 5, 4, 0, 4, 6, 6, 7, 6, 8, 9, 8, 8, 7, 4, 9, 9, 9, 5, 2, 8, 8, 5, 6, 7, 4, 4},
                    /* 23 */ {5, 5, 2, 10, 8, 9, 6, 7, 6, 7, 6, 6, 4, 8, 10, 9, 8, 8, 6, 5, 6, 8, 4, 0, 3, 6, 6, 4, 8, 8, 4, 6, 6, 4, 6, 8, 7, 4, 2, 8, 8, 5, 5, 7, 7, 8},
                    /* 24 */ {2, 7, 5, 11, 5, 7, 8, 5, 7, 8, 4, 7, 4, 9, 10, 10, 11, 6, 9, 2, 7, 8, 6, 3, 0, 8, 9, 4, 7, 8, 7, 7, 9, 7, 8, 10, 7, 7, 5, 7, 9, 6, 7, 10, 5, 6},
                    /* 25 */ {8, 5, 8, 5, 6, 8, 8, 5, 7, 7, 7, 8, 4, 7, 5, 4, 8, 8, 6, 7, 9, 8, 6, 6, 8, 0, 6, 9, 6, 3, 8, 10, 8, 2, 8, 11, 5, 8, 4, 4, 2, 6, 9, 5, 7, 5},
                    /* 26 */ {7, 5, 4, 9, 5, 4, 5, 8, 5, 3, 5, 8, 7, 7, 6, 4, 6, 4, 6, 7, 4, 6, 7, 6, 9, 6, 0, 6, 4, 9, 5, 7, 4, 8, 6, 9, 7, 2, 8, 2, 4, 7, 7, 9, 7, 7},
                    /* 27 */ {2, 7, 2, 10, 9, 10, 8, 9, 6, 8, 4, 7, 8, 7, 9, 9, 8, 6, 6, 6, 6, 8, 6, 4, 4, 9, 6, 0, 6, 8, 7, 6, 6, 7, 4, 8, 8, 4, 5, 8, 9, 6, 5, 7, 5, 6},
                    /* 28 */ {5, 7, 6, 8, 8, 8, 9, 10, 6, 7, 6, 7, 4, 5, 6, 5, 6, 7, 7, 7, 6, 6, 8, 8, 7, 6, 4, 6, 0, 9, 5, 8, 8, 8, 2, 8, 6, 6, 10, 2, 4, 7, 3, 5, 5, 4},
                    /* 29 */ {10, 8, 6, 8, 9, 8, 8, 8, 10, 10, 10, 11, 7, 10, 8, 7, 11, 6, 9, 10, 7, 11, 9, 8, 8, 3, 9, 8, 9, 0, 11, 10, 10, 5, 11, 12, 8, 8, 7, 7, 5, 9, 9, 8, 10, 8},
                    /* 30 */ {9, 8, 6, 7, 8, 6, 8, 9, 5, 7, 10, 8, 8, 6, 8, 9, 7, 9, 10, 9, 7, 10, 8, 4, 7, 8, 5, 7, 5, 11, 0, 6, 9, 6, 3, 9, 7, 7, 6, 6, 8, 9, 4, 6, 6, 9},
                    /* 31 */ {5, 5, 4, 11, 7, 5, 2, 9, 8, 7, 7, 9, 10, 10, 8, 10, 10, 4, 8, 8, 7, 9, 8, 6, 7, 10, 7, 6, 8, 10, 6, 0, 8, 8, 9, 3, 11, 6, 6, 9, 8, 9, 7, 10, 7, 7},
                    /* 32 */ {7, 8, 4, 9, 6, 8, 6, 9, 6, 7, 5, 9, 9, 6, 8, 7, 10, 8, 8, 9, 4, 10, 7, 6, 9, 8, 4, 6, 8, 10, 9, 8, 0, 10, 6, 9, 9, 2, 8, 6, 8, 9, 7, 9, 9, 7},
                    /* 33 */ {8, 5, 6, 7, 8, 9, 6, 3, 9, 7, 6, 6, 6, 8, 7, 6, 7, 8, 4, 5, 8, 8, 4, 4, 7, 2, 8, 7, 8, 5, 6, 8, 10, 0, 6, 9, 7, 8, 2, 6, 4, 5, 9, 3, 7, 7},
                    /* 34 */ {6, 8, 6, 6, 8, 9, 9, 8, 7, 9, 7, 8, 6, 3, 5, 7, 8, 8, 9, 9, 4, 8, 9, 6, 8, 8, 6, 4, 2, 11, 3, 9, 6, 6, 0, 6, 4, 4, 8, 4, 6, 6, 4, 3, 6, 6},
                    /* 35 */ {8, 7, 6, 8, 10, 8, 5, 6, 10, 9, 8, 7, 12, 8, 9, 12, 7, 7, 10, 9, 5, 9, 9, 8, 10, 11, 9, 8, 8, 12, 9, 3, 9, 9, 6, 0, 10, 7, 9, 10, 10, 6, 7, 9, 5, 10},
                    /* 36 */ {9, 6, 8, 6, 8, 8, 9, 10, 4, 6, 9, 10, 3, 3, 6, 5, 9, 9, 9, 9, 6, 8, 9, 7, 7, 5, 7, 8, 6, 8, 7, 11, 9, 7, 4, 10, 0, 8, 9, 5, 3, 6, 7, 5, 8, 8},
                    /* 37 */ {5, 7, 2, 8, 7, 6, 7, 7, 4, 5, 3, 7, 7, 5, 6, 5, 8, 6, 6, 9, 2, 8, 5, 4, 7, 8, 2, 4, 6, 8, 7, 6, 2, 8, 4, 7, 8, 0, 6, 4, 6, 8, 5, 7, 7, 5},
                    /* 38 */ {6, 3, 4, 9, 6, 7, 4, 5, 7, 5, 4, 4, 6, 6, 9, 8, 9, 6, 6, 3, 6, 6, 2, 2, 5, 4, 8, 5, 10, 7, 6, 6, 8, 2, 8, 9, 9, 6, 0, 8, 6, 3, 7, 5, 5, 6},
                    /* 39 */ {7, 5, 6, 9, 7, 6, 7, 8, 7, 5, 7, 6, 6, 7, 4, 4, 4, 6, 7, 5, 6, 4, 8, 8, 7, 4, 2, 8, 2, 7, 6, 9, 6, 6, 4, 10, 5, 4, 8, 0, 2, 6, 5, 7, 7, 6},
                    /* 40 */ {7, 3, 8, 7, 7, 7, 6, 7, 5, 5, 9, 7, 6, 6, 3, 2, 6, 6, 6, 7, 7, 6, 8, 8, 9, 2, 4, 9, 4, 5, 8, 8, 8, 4, 6, 10, 3, 6, 6, 2, 0, 4, 7, 7, 5, 7},
                    /* 41 */ {4, 6, 7, 6, 8, 6, 7, 8, 6, 4, 6, 7, 8, 3, 7, 6, 10, 8, 7, 6, 6, 8, 5, 5, 6, 6, 7, 6, 7, 9, 9, 9, 9, 5, 6, 6, 6, 8, 3, 6, 4, 0, 7, 8, 7, 8},
                    /* 42 */ {5, 4, 3, 7, 9, 7, 7, 10, 3, 5, 7, 4, 6, 7, 9, 8, 9, 8, 7, 9, 6, 6, 6, 5, 7, 9, 7, 5, 3, 9, 4, 7, 7, 9, 4, 7, 7, 5, 7, 5, 7, 7, 0, 7, 2, 7},
                    /* 43 */ {8, 8, 9, 9, 9, 7, 9, 5, 9, 9, 7, 9, 8, 6, 8, 5, 10, 8, 7, 8, 7, 11, 7, 7, 10, 5, 9, 7, 5, 8, 6, 10, 9, 3, 3, 9, 5, 7, 5, 7, 7, 8, 7, 0, 9, 5},
                    /* 44 */ {3, 2, 5, 9, 8, 6, 5, 9, 5, 4, 5, 2, 8, 9, 8, 7, 9, 7, 6, 7, 8, 4, 4, 7, 5, 7, 7, 5, 5, 10, 6, 7, 9, 7, 6, 5, 8, 7, 5, 7, 5, 7, 2, 9, 0, 7},
                    /* 45 */ {4, 8, 7, 8, 4, 6, 5, 8, 8, 6, 2, 6, 8, 5, 8, 9, 6, 3, 3, 7, 5, 8, 4, 8, 6, 5, 7, 6, 4, 8, 9, 7, 7, 7, 6, 10, 8, 5, 6, 6, 7, 8, 7, 5, 7, 0},
            };

    private static GRBVar[][] xij;

    // Vincoli MTZ
    private static GRBVar[] u;

    private static final int vertex = 26;

    private static final double a_percentage = 0.06;

    private static final int side_b1 = 30;
    private static final int side_b2 = 16;

    private static final int optimum_cost = 132;

    private static final int side_d1 = 10;
    private static final int side_d2 = 45;

    private static final int side_e1 = 42;
    private static final int side_e2 = 21;

    private static final int side_f1 = 36;
    private static final int side_f2 = 25;

    private static final int side_g1 = 21;
    private static final int side_g2 = 11;

    private static final int side_h1 = 1;
    private static final int side_h2 = 44;

    private static final int side_i1 = 4;
    private static final int side_i2 = 5;

    private static final int extra_cost = 2;

    private static int MATRIX_SIZE = 46;


    public static void main(String[] args) throws GRBException {
        try {

            GRBEnv env = new GRBEnv("singolo16.log");

            settingMyParameters(env);

            GRBModel model = new GRBModel(env);
            model.set(GRB.IntParam.OutputFlag, 0);

            header();

            // Matrice delle variabili del problema da ottimizzare, la loro somma deve essere minima.
            xij = settingMyVariables(model);

            // Variabili MTZ
            u = settingMyMTZVariables(model);

            // Funzione obiettivo del problema da ottimizzare.
            setLossFunction(model, xij);

            // Vincoli del problema da ottimizzare.
            setConstraint1(model, xij);
            setConstraint2(model, xij);
            setConstraint3(model, xij);
            setMTZConstraint(model, xij, u);

            // Faccio ottimizzare il problema a Gurobi.
            solve(model);

            // Salvtaggio del primo percorso ottimo trovato.
            ArrayList<Integer> firstQuestionOptimumPath = new ArrayList<>();
            double objVal1 = model.get(GRB.DoubleAttr.ObjVal);

            // Stampo a video i risultati ottenuti
            firstQuestion(xij, firstQuestionOptimumPath, objVal1);




            /*
             * Aggiungo un vincolo al model, perché voglio una nuova soluzione
             * ottima del problema. Questa volta, però, è richiesto un vincolo aggiuntivo,
             * ossia che il percorso ottimo abbia costo pari a quello di objVal.
             */
            setConstraint4(model, xij, objVal1);

            // Faccio ottimizzare il problema a Gurobi.
            solve(model);

            // Numero delle soluzioni ottime trovate
            int solCount = model.get(GRB.IntAttr.SolCount);

            // Salvtaggio del primo percorso ottimo trovato.
            ArrayList<Integer> secondQuestionOptimumPath = new ArrayList<>();

            // Stampo a video i risultati ottenuti.
            secondQuestion(model, xij, firstQuestionOptimumPath, secondQuestionOptimumPath, solCount);



            /*
             * Ridefinisco il model, perché voglio una nuova soluzione ottima del problema.
             * Questa volta, però, sono richiesti più vincoli aggiuntivi.
             */
            model.reset();
            model.set(GRB.IntParam.OutputFlag, 0);

            // Matrice delle variabili del problema da ottimizzare, la loro somma deve essere minima.
            xij = settingMyVariables(model);

            // Variabili MTZ
            u = settingMyMTZVariables(model);

            // Funzione obiettivo del problema da ottimizzare.
            setLossFunction(model, xij);

            // Vincoli del problema da ottimizzare.
            setConstraint1(model, xij);
            setConstraint2(model, xij);
            setConstraint3(model, xij);
            setMTZConstraint(model, xij, u);
            setConstraintA(model, xij);
            setConstraintB1(model, xij);
            setConstraintB2(model, xij);
            setConstraintC(model, xij);
            // La richiesta D la risolvo nel quesito III.

            // Faccio ottimizzare il problema a Gurobi.
            solve(model);

            // Salvataggio del primo percorso ottimo trovato.
            ArrayList<Integer> thirdQuestionOptimumPath = new ArrayList<>();
            thirdQuestion(model, xij, thirdQuestionOptimumPath);

            model.dispose();
            env.dispose();
        } catch (GRBException e) {
            System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
        }
    }

    private static void settingMyParameters(GRBEnv env) throws GRBException {
        /*
         * In base a ciò che riporta il forum di Gurobi, ho trovato
         * che se imposto «GRB.IntParam.PoolSolutions», allora ho la
         * possibilità di trovare (al massimo) le dieci migliori
         * soluzioni al problema. Se le soluzioni sono meno, Gurobi
         * mi informa su quale sia il numero esatto.
         *
         * https://support.gurobi.com/hc/en-us/community/posts/360073084831-Solution-count
         * https://www.gurobi.com/documentation/9.1/refman/finding_multiple_solutions.html
         */
        env.set(GRB.IntParam.PoolSearchMode, 2);
        env.set(GRB.IntParam.PoolSolutions, 10);
        env.set(GRB.DoubleParam.PoolGap, 0.1);
    }

    /**
     * Imposto i parametri del modello del problema di ottimizzazione.
     *
     * @param model modello del problema di ottimizzazione.
     * @return xij  matrice binaria dei percorsi attivi.
     * @throws GRBException
     */
    private static GRBVar[][] settingMyVariables(GRBModel model) throws GRBException {
        GRBVar[][] xij = new GRBVar[MATRIX_SIZE][MATRIX_SIZE];

        for (int i = 0; i < MATRIX_SIZE; i++)
            for (int j = 0; j < MATRIX_SIZE; j++)
                xij[i][j] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "x_" + i + j);

        return xij;
    }


    /**
     * Imposto i parametri del modello del problema di ottimizzazione.
     *
     * @param model modello del problema di ottimizzazione.
     * @return xij  matrice binaria dei percorsi attivi.
     * @throws GRBException
     */
    private static GRBVar[] settingMyMTZVariables(GRBModel model) throws GRBException {
        GRBVar[] u = new GRBVar[MATRIX_SIZE];

        for(int i = 0; i < MATRIX_SIZE; i++)
            u[i] = model.addVar(1, MATRIX_SIZE, 0, GRB.INTEGER, "u_" + i);

        return u;
    }


    /**
     * Costruzione della funzione obiettivo, la cui struttura è
     * min sommatoria^n_{i=1} sommatoria^n_{j=1} c_{ij} * x_{ij}
     *
     * @param model modello del problema di ottimizzazione.
     * @throws GRBException
     */
    private static void setLossFunction(GRBModel model, GRBVar[][] xij) throws GRBException {
        GRBLinExpr lossFunction = new GRBLinExpr();

        for (int i = 0; i < MATRIX_SIZE; i++)
            for (int j = 0; j < MATRIX_SIZE; j++)
                lossFunction.addTerm(costs[i][j], xij[i][j]);

        model.setObjective(lossFunction);
        model.set(GRB.IntAttr.ModelSense, GRB.MINIMIZE);
    }


    /**
     * Decreto che ogni nodo può avere una ed una sola uscita.
     * Assieme a {@code setConstraint2} mi permette di impostare la
     * condizione per cui ogni nodo va percorso una ed una sola volta.
     *
     * @param model modello del problema di ottimizzazione.
     * @param xij   matrice binaria dei percorsi attivi.
     * @throws GRBException
     *
     */
    private static void setConstraint1(GRBModel model, GRBVar[][]xij) throws GRBException {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            GRBLinExpr expr = new GRBLinExpr();

            for (int j = 0; j < MATRIX_SIZE; j++)
                expr.addTerm(1, xij[i][j]);

            model.addConstr(expr, GRB.EQUAL, 1, "1! uscita per ogni vertice");
        }
    }


    /**
     * Decreto che ogni nodo può avere una ed una sola entrata.
     * Assieme a {@code setConstraint1} mi permette di impostare la
     * condizione per cui ogni nodo va percorso una ed una sola volta.
     *
     * @param model modello del problema di ottimizzazione.
     * @param xij   matrice binaria dei percorsi attivi.
     * @throws GRBException
     *
     */
    private static void setConstraint2(GRBModel model, GRBVar[][]xij) throws GRBException {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            GRBLinExpr expr = new GRBLinExpr();
            for (int j = 0; j < MATRIX_SIZE; j++)
                expr.addTerm(1, xij[j][i]);

            model.addConstr(expr, GRB.EQUAL, 1, "1! entrata per ogni vertice");
        }
    }


    /**
     * Non permetto la percorribilità della diagonale della matrice delle
     * adiacenze, in quanto non posso entrare ed uscire nello stesso momento
     * dallo stesso nodo. Questo vincolo sarebbe stato ridondante nel caso
     * in cui il costo del nodo verso sé stesso fosse molto alto, ad esempio
     * 99 in questo problema specifico, così da scoraggiare Gurobi dall'intraprendere
     * quel path. Tuttavia, siccome valori alti possono creare instabilità
     * (facendo un test ho notato che effettivamente c'erano delle imprecisioni nel
     * valore dopo la virgola, anche se minime), preferisco settare la diagonale
     * a zero e poi aggiungere questo vincolo.
     *
     * @param model modello del problema di ottimizzazione.
     * @param xij   matrice binaria dei percorsi attivi.
     * @throws GRBException
     *
     */
    private static void setConstraint3(GRBModel model, GRBVar[][]xij) throws GRBException {
        GRBLinExpr expr = new GRBLinExpr();
        for (int i = 0; i < MATRIX_SIZE; i++) {
            expr = new GRBLinExpr();
            expr.addTerm(1, xij[i][i]);
            model.addConstr(expr, GRB.EQUAL, 0, "diagonale non percorribile");
        }
    }

    /**
     * Chiedo a Gurobi di trovarmi il(i) percorso(i) tale(i) che
     * il costo totale sia pari a quello del percorso del quesito 1.
     * Potrei evitare? Sì, tuttavia mi permette di limitare il pool
     * delle soluzioni (da 9 passa a 4) e, quindi, escludere quei
     * valori che differiscono da objVal anche se di molto poco.
     * Ad esempio, avevo notato un 132 fra i valori ottimi durante
     * la modellizzazione del Quesito I, che era una delle soluzioni
     * peggiori fra le migliori. In questo modo, evito errori,
     * anche se marginali.
     *
     * @param model   modello del problema di ottimizzazione.
     * @param xij     matrice binaria dei percorsi attivi.
     * @param objVal  valore del tsp del quesito I.
     *
     */
    private static void setConstraint4(GRBModel model, GRBVar[][] xij, double objVal) throws GRBException {
        GRBLinExpr expr = new GRBLinExpr();
        for(int i=0; i < MATRIX_SIZE; i++)
            for (int j = 0; j < MATRIX_SIZE; j++)
                expr.addTerm(costs[i][j], xij[i][j]);

        model.addConstr(expr, GRB.EQUAL, objVal, " expr = " + objVal);
    }


    /**
     * @param model   modello del problema di ottimizzazione.
     * @param xij     matrice binaria dei percorsi attivi.
     *
     * Il costo dei lati incidenti a v deve essere al massimo il a% del costo totale del ciclo.
     *
     * In questo quesito ho imposto che tutti i lati incidenti, quindi entranti ed uscenti da v,
     * abbiano un costo pari ad a% del costo totale.
     */
    private static void setConstraintA(GRBModel model, GRBVar[][] xij) throws GRBException {
        GRBLinExpr expr = new GRBLinExpr();
        for(int i = 0; i < MATRIX_SIZE; i++) {
            expr.addTerm(costs[i][vertex], xij[i][vertex]);
            expr.addTerm(costs[vertex][i], xij[vertex][i]);
        }

        for(int i = 0; i < MATRIX_SIZE; i++)
            for (int j = 0; j < MATRIX_SIZE; j++)
                expr.addTerm((-1) * costs[i][j] * a_percentage, xij[i][j]);

        model.addConstr(expr, GRB.LESS_EQUAL, 0 , "se lato incidente a " + vertex + " => a% del costo costo totale del ciclo");
    }


    /**
     * Se il lato (b1,b2) viene percorso, il costo del ciclo ottimo deve essere inferiore a c.
     *
     * L'espressione matematica di questo vincolo è
     *
     *                sum^n_{i=0} sum^n_{j=0} c[i][j] * x[i][j] ≤ x[b_1][b_2] * c + M * (1 - x[b_1][b_2])
     *                sum^n_{i=0} sum^n_{j=0} c[i][j] * x[i][j] - c * x[b_1][b_2] + M * x[b_1][b_2]) ≤ M
     *
     * dove M serve per il vincolo di dipendenza — se si verifica la condizione «percorrere lato
     * (b1,b2)», allora si attiva, altrimenti è come se non ci fosse e diventa ridondante.
     *
     * @param model   modello del problema di ottimizzazione.
     * @param xij     matrice binaria dei percorsi attivi.
     *
     */
    private static void setConstraintB1(GRBModel model, GRBVar[][] xij) throws GRBException {
        GRBLinExpr expr = new GRBLinExpr();
        for(int i=0; i< MATRIX_SIZE; i++)
            for (int j = 0; j < MATRIX_SIZE; j++)
                expr.addTerm(costs[i][j], xij[i][j]);

        // Serve un valore alto a sufficienza perché il vincolo sia ridondante nel caso in cui non venga verificato.
        int M = findTheBigMConst();

        expr.addTerm((-1) * optimum_cost, xij[side_b1][side_b2]);
        expr.addTerm(M, xij[side_b1][side_b2]);
        model.addConstr(expr, GRB.LESS_EQUAL, M,"xij[b1][b2] == 1 => cost ≤ optimum_cost");
    }


    /**
     * Se il lato (b1,b2) viene percorso, il costo del ciclo ottimo deve essere inferiore a c.
     *
     * L'espressione matematica di questo vincolo è
     *
     *                sum^n_{i=0} sum^n_{j=0} c[i][j] * x[i][j] ≤ x[b_2][b_1] * c + M * (1 - x[b_2][b_1])
     *                sum^n_{i=0} sum^n_{j=0} c[i][j] * x[i][j] - c * x[b_2][b_1] - M + M * x[b_2][b_1]) ≤ 0
     *
     * dove M serve per il vincolo di dipendenza — se si verifica la condizione «percorrere lato
     * (b1,b2), allora si attiva, altrimenti è come se non ci fosse e diventa ridondante.
     *
     * @param model   modello del problema di ottimizzazione.
     * @param xij     matrice binaria dei percorsi attivi.
     *
     */
    private static void setConstraintB2(GRBModel model, GRBVar[][] xij) throws GRBException {
        GRBLinExpr expr = new GRBLinExpr();
        for(int i=0; i< MATRIX_SIZE; i++)
            for (int j = 0; j < MATRIX_SIZE; j++)
                expr.addTerm(costs[i][j], xij[i][j]);

        // Serve un valore alto a sufficienza perché il vincolo sia ridondante nel caso in cui non venga verificato.
        int M = findTheBigMConst();

        expr.addTerm((-1) * optimum_cost, xij[side_b2][side_b1]);
        expr.addTerm(M, xij[side_b2][side_b1]);
        model.addConstr(expr, GRB.LESS_EQUAL, M,"xij[b2][b1] == 1 => cost ≤ optimum_cost");
    }


    /**
     * Il lato (d1, d2) sia percorribile solo nel caso in cui vengano attivati anche i percorsi (e1, e2) e (f1, f2)
     *
     *                             IMPLEMENTAZIONE
     *                2 * xij[d1][d2] - xij[e1][e2] - xij[f1][f2] ≤ 0
     *
     * @param model   modello del problema di ottimizzazione.
     * @param xij     matrice binaria dei percorsi attivi.
     *
     */
    private static void setConstraintC(GRBModel model, GRBVar[][] xij) throws GRBException {
        GRBLinExpr expr = new GRBLinExpr();

        /*
         * Devo considerare tutte le casistiche possibili. Infatti,
         * non è detto che il nodo d1-d2 non venga percorso come
         * d2-d1, stessa cosa per gli altri. Ricordo che nel
         * modello è implementato che ogni nodo può essere percorso
         * 1! in ingresso e 1! in uscita, però, finché non avviene
         * la modellizzazione, non sono a conoscenza del verso
         * in cui avviene il percorso, devo tenere aperte tutte
         * le opzioni.
         */
        expr.addTerm(2, xij[side_d1][side_d2]);
        expr.addTerm(2, xij[side_d2][side_d1]);
        expr.addTerm(-1, xij[side_f1][side_f2]);
        expr.addTerm(-1, xij[side_f2][side_f1]);
        expr.addTerm(-1, xij[side_e1][side_e2]);
        expr.addTerm(-1, xij[side_e2][side_e1]);

        model.addConstr(expr, GRB.LESS_EQUAL, 0, "xij[" + side_d1 + "][" + side_d2 + "] == 1 <=> xij[" + side_f1 + "][" + side_f2 + "] == 1 && xij[" + side_e1 + "][" + side_e2 + "]");
    }


    /**
     * Uso i vincoli Miller-Tucker-Zemlin, che sono un’alternativa ai vincoli di sub-tour
     * elimination/connectivity. Le variabli ausiliarie permettono di stabilire un ordine
     * cronologico di visita dei nodi. Questi vincoli prevedono che ui sia la posizione del
     * nodo i all’interno del ciclo ottimo. I vincoli che vado a stabilire garantiscono
     * l’assenza di sottocicli.
     *
     * @param model modello del problema di ottimizzazione.
     * @param xij   matrice binaria dei percorsi attivi.
     * @param u     le variabili MTZ.
     * @throws GRBException
     */
    private static void setMTZConstraint(GRBModel model, GRBVar[][]xij, GRBVar[]u) throws GRBException {
        GRBLinExpr expr = new GRBLinExpr();

        /*            VINCOLO
         *            u[0] = 1
         */
        expr.addTerm(1, u[0]);
        model.addConstr(expr, GRB.EQUAL, 1, "valore di u[1]");

        /*            VINCOLO
         *          2 ≤ u[i] ≤ n
         */
        for (int i = 1; i < MATRIX_SIZE - 1; i++) {
            expr = new GRBLinExpr();
            expr.addTerm(1, u[i]);
            model.addConstr(expr, GRB.GREATER_EQUAL, 2, "u[" + i +"] ≥ 2");
            expr = new GRBLinExpr();
            expr.addTerm(1, u[i]);
            model.addConstr(expr, GRB.LESS_EQUAL, MATRIX_SIZE, "u[" + i +"] ≤ " + MATRIX_SIZE);
        }

        /*
         *             VINCOLO
         * u[j] ≥ u[i] + 1 + (n - 1) * (1 - x[i][j])
         * u[j] ≥ u[i] + 1 + n - nx[i][j] - 1 + x[i][j]
         * u[j] - u[i] ≥ n + x[i][j] * (1 - n)
         * u[j] - u[i] + (n - 1) * x[i][j] ≥ n
         *
         *
         *             UPDATE
         * in altra forma, si può esprimere come
         * cfr https://co-enzyme.fr/blog/traveling-salesman-problem-tsp-in-cplex-opl-with-miller-tucker-zemlin-mtz-formulation/
         * u[i] + x[i][j] ≤ u[j] + (n - 1) * (1 - x[i][j])
         * u[i] + x[i][j] ≤ u[j] + n - n * x[i][j] - 1 + x[i][j]
         * u[i] ≤ u[j] + n - n * x[i][j] - 1
         * u[i] - u[j] ≤ + (n - 1) - n * x[i][j]
         * u[i] - u[j] + n * x[i][j] ≤ (n - 1)
         *
         * Ho notato che in questo modo le iterazioni sono maggiori
         * e viene anche utilizzato il cutting planes. Tuttavia,
         * la funzione obiettivo è la stessa (131 contro 130.99999999999997 ≈ 131)
         */
        for(int i = 1; i < MATRIX_SIZE; i++)
            for (int j = 1; j < MATRIX_SIZE; j++) {
                if (i != j) {
                    expr = new GRBLinExpr();
                    expr.addTerm(1, u[i]);
                    expr.addTerm(-1, u[j]);
                    expr.addTerm(MATRIX_SIZE, xij[i][j]);
                    model.addConstr(expr, GRB.LESS_EQUAL, MATRIX_SIZE - 1, "vincolo_ordine_" + i + j);
                }
            }
    }


    /**
     * Metodo che serve per arrotondare alla quarta cifra decimale.
     *
     * @param value valore da arrotondare
     * @return      il valore arrotondato
     *
     */
    private static double round(double value){
        int exp = 4;
        int base = 10;
        return (Math.round(value * Math.pow(base, exp)) / Math.pow(base, exp));
    }


    /**
     * Il big M è il valore sufficientemente grande tale da soddisfare il vincolo.
     * Ho stabilito che deve essere pari alla somma dei valori della triangolare superiore.
     *
     * @return il big M
     */
    private static int findTheBigMConst() {
        int M = 0;

        for(int i = 0; i < MATRIX_SIZE / 2; i++)
            for(int j = 0; j < MATRIX_SIZE / 2; j++)
                M += costs[i][j];

        return M;
    }

    private static void header() {
        System.out.println("\n\n\n\n\n\nGRUPPO singolo 16\nComponenti: Contestabile");
    }


    /**
     * Soluzione e stampa a video del primo quesito.
     *
     * @param xij          matrice binaria dei percorsi attivi.
     * @param optimumPath  percorso ottimo trovato da Gurobi.
     * @param objVal       valore della funzione obiettivo.
     * @throws GRBException
     *
     */
    private static void firstQuestion(GRBVar[][]xij, ArrayList<Integer> optimumPath, double objVal) throws GRBException {
        System.out.println("\n\n\n\n\n\nQUESITO I:");
        System.out.println("Funzione obiettivo: " + round(objVal));
        System.out.print("Ciclo ottimo 1 = ");

        storeThePath(optimumPath, xij);

        System.out.println(optimumPath);
    }

    /**
     * Soluzione e stampa a video del secondo quesito.
     * Quando faccio fare a Gurobi la modellizzazione di questo quesito, mi dice che non solo ho trovato
     * la soluzione ottima, ma anche altre 4 soluzioni ammissibili aventi funzione obiettivo uguale
     * a quella del quesito I — https://support.gurobi.com/hc/en-us/community/posts/360073084831-Solution-count
     *
     * Dopo l'ottimizzazione, ritorno una delle possibili soluzioni settando il parametro SolutionNumber,
     * poi interrogo gli attributi Xn e PoolObjVal.
     *
     * @param model         modello del problema di ottimizzazione.
     * @param xij           matrice binaria dei percorsi attivi.
     * @param optimumPath1  percorso ottimo trovato da Gurobi per il Quesito I.
     * @param optimumPath2  percorso ottimo trovato da Gurobi per il Quesito II.
     * @param solCount      numero delle soluzioni ottime trovate da Gurobi.
     * @throws GRBException
     */
    private static void secondQuestion(GRBModel model, GRBVar[][]xij, ArrayList<Integer> optimumPath1, ArrayList<Integer> optimumPath2, int solCount) throws GRBException {
        System.out.println("\n\n\n\n\n\nQUESITO II:");
        System.out.print("Ciclo ottimo 2 = ");

        // Salvo la soluzione che gurobi mi dà.
        storeThePath(optimumPath2, xij);
        ArrayList<Integer> optimumPath;

        /*
         * Controllo che gurobi non mi abbia nuovamente ritornato come prima soluzione quella del quesito I.
         * In caso affermativo, a me non va bene: la conosco già, ne volevo un'altra.
         */
        if(optimumPath1.equals(optimumPath2))
            /*
             * Interrogo la variabile solCount, che mi salva il numero di soluzioni ottime trovate.
             * Ce n'è più di una? Bene, allora mi basta ritornare una di quelle.
             */
            if(solCount != 1) {
                for (int i = 2; i < solCount; i++) {
                    model.set(GRB.IntParam.SolutionNumber, i); // una delle soluzioni, ma con funzione obiettivo uguale a quesito I.
                    //System.out.println("fo = " + model.get(GRB.DoubleAttr.PoolObjVal)); Test per vedere se fo1 = fo2, è giusto.

                    // Valore delle variabili trovate con model.set(GRB.IntParam.SolutionNumber, i).
                    GRBVar[] fvars = model.getVars();
                    double[] x = model.get(GRB.DoubleAttr.Xn, fvars);

                    // Converto l'array in matrice per poter poi trovare il path corretto.
                    double[][] newMatrix = conversion(x);

                    ArrayList<Integer> newOptimumPath = new ArrayList<>();
                    storeThePath2(newOptimumPath, newMatrix);

                    /*
                     * Controllo che i due percorsi siano differenti.
                     * Lo sono? Bene, posso stampare il nuovo ciclo trovato.
                     * Non lo sono? Comprendo che l'unico modo effettivo per
                     * avere costo minimo e visitare tutti i nodi sia quello
                     * di percorrerlo all'ìncontrario, quindi mi salvo
                     * in optimum path il percorso inverso, dato che la
                     * matrice dei costi è simmetrica.
                     */
                    boolean differentPath;
                    if (!optimumPath1.equals(newOptimumPath))
                        differentPath = true;
                    else differentPath = false;

                    if (differentPath) {
                        optimumPath = newOptimumPath;
                        System.out.println(optimumPath);
                        break;
                    }
                    else
                        continue;
                }
            } else
                System.out.println("Non esiste un altro percorso ottimo.");
        else {
            optimumPath = optimumPath2;
            System.out.println(optimumPath);
        }
    }


    /**
     * Soluzione e stampa a video del terzo quesito.
     *
     * @param model         modello del problema di ottimizzazione.
     * @param xij           matrice binaria dei percorsi attivi.
     * @param optimumPath3  percorso ottimo trovato da Gurobi per il Quesito III.
     * @throws GRBException
     */
    private static void thirdQuestion(GRBModel model, GRBVar[][]xij, ArrayList<Integer> optimumPath3) throws GRBException {
        System.out.printf("\n\n\n\n\n\nQUESITO III:\n");
        //System.out.println("Obiettivo4Real: " + round(model.get(GRB.DoubleAttr.ObjVal)));
        System.out.printf("Funzione obiettivo: ");

        //boolean checkIfActive = false;
        /*
         * Controllo il percorso trovato da gurobi: nel caso in cui i lati (g1,g2), (h1,h2) e (i1,i2) vengano
         * percorsi, allora stampo la funzione obiettivo aggiungendo la penalità nel costo totale.
         */
        if (    (xij[side_g1][side_g2].get(GRB.DoubleAttr.X) == 1 || xij[side_g2][side_g1].get(GRB.DoubleAttr.X) == 1) &&
                (xij[side_h1][side_h2].get(GRB.DoubleAttr.X) == 1 || xij[side_h2][side_h1].get(GRB.DoubleAttr.X) == 1) &&
                (xij[side_i1][side_i2].get(GRB.DoubleAttr.X) == 1 || xij[side_i2][side_i1].get(GRB.DoubleAttr.X) == 1)    ) {
            //checkIfActive = true;
            System.out.print(round(model.get(GRB.DoubleAttr.ObjVal) + extra_cost) + "\n");
            //System.out.println("ciVaONo: " + (checkIfActive ? "sì" : "no"));
        }

        else {
            System.out.print(round(model.get(GRB.DoubleAttr.ObjVal)) + "\n");
            //System.out.println("ciVaONo: " + (checkIfActive ? "sì" : "no"));
        }

        /*
        boolean ilB = false;
        if ((xij[side_b1][side_b2].get(GRB.DoubleAttr.X) == 1 || xij[side_b2][side_b1].get(GRB.DoubleAttr.X) == 1)) ilB = true;
        System.out.println("ilB: " + (ilB ? "sì" : "no"));
         */

        System.out.print("Ciclo ottimo 3 = ");

        // Salvo la soluzione che gurobi mi dà.
        storeThePath(optimumPath3, xij);
        System.out.print(optimumPath3);
    }

    private static void solve (GRBModel model) throws GRBException {
        model.optimize();
    }

    /**
     * Mi permette di visualizzare il percorso trovato da Gurobi
     * e memorizzarlo in un Arraylist.
     *
     * @param optimumPath percorso ottimo.
     * @param xij         matrice binaria dei percorsi attivi.
     * @throws GRBException
     */
    private static void storeThePath (ArrayList<Integer> optimumPath, GRBVar[][] xij) throws GRBException {
        // Il primo nodo che percorre, ossia da dove parte il percorso, è il nodo 0.
        int nowImHere = 0;
        optimumPath.add(nowImHere);

        /*
         * Questo for serve per essere certi che vengano controllati
         * tutti e 46 i vertici da percorrere
         */
        for (int i = 0; i < MATRIX_SIZE; i++)
            for (int j = 0; j < MATRIX_SIZE; j++) {
                /*
                 * Se la variabile binaria è pari ad 1, allora il link fra i nodi è attivo.
                 * La ragione per cui nowImHere è indice della riga della matrice è
                 * dovuta al fatto che devo stabilire dal nodo in cui mi trovo,
                 * che è nowImHere, qual è il link ottimo: questo lavoro l'ha fatto
                 * Gurobi, quindi mi basta trovare quella xij[nowImHere][j] tale che
                 * la variabile binaria sia pari ad 1.
                 */
                if (xij[nowImHere][j].get(GRB.DoubleAttr.X) == 1) {
                    /*
                     * Questo frammento di codice svolge un'operazione fondamentale. Gurobi
                     * ha stabilito che fra il vertice nowImHere e j c'è un percorso che è il migliore,
                     * quindi lo aggiungo al mio arraylist che salva tutti i nodi visitati in
                     * ordine di passaggio. Il commesso viaggiatore parte da zero, quindi la mia
                     * variabile nowImHere è settata a zero. Proseguendo, però, chiaramente
                     * il commesso viaggia e si sposta in un altro nodo, quindi nowImHere
                     * va aggiornata, ma con cosa? Siccome ho stabilito nell'if che il link è
                     * attivo, allora il commesso si è spostato in j, quindi nowImHere = j
                     * e prosegue il suo percorso, incrementando nuovamente i e trovando dove
                     * si sposterà successivamente, così fino a che il percorso non viene concluso
                     * e il commesso viaggiatore si trova nuovamente in nowImHere = 0,
                     * concludendo il ciclo Hamiltoniano.
                     */
                    optimumPath.add(j);
                    nowImHere = j;
                    break; // mi serve per uscire dal for e proseguire con il for esterno
                }
                //continue; non va bene, continua il for annidato!!!
            }
    }

    /**
     * Mi permette di visualizzare il percorso trovato da Gurobi
     * e memorizzarlo in un Arraylist.
     * È la versione implementata specificamente per il secondo quesito,
     * in quanto non avevo più una matrice di tipo GRBVar, ma double.
     *
     * @param optimumPath percorso ottimo.
     * @param xij         matrice binaria dei percorsi attivi.
     * @throws GRBException
     */
    private static void storeThePath2 (ArrayList<Integer> optimumPath, double[][] xij) {
        // Il primo nodo che percorre, ossia da dove parte il percorso, è il nodo 0.
        int nowImHere = 0;
        optimumPath.add(nowImHere);

        /*
         * Questo for serve per essere certi che vengano controllati
         * tutti e 46 i vertici da percorrere
         */
        for (int i = 0; i < MATRIX_SIZE; i++)
            for (int j = 0; j < MATRIX_SIZE; j++) {
                /*
                 * Se la variabile binaria è pari ad 1, allora il link fra i nodi è attivo.
                 * La ragione per cui nowImHere è indice della riga della matrice è
                 * dovuta al fatto che devo stabilire dal nodo in cui mi trovo,
                 * che è nowImHere, qual è il link ottimo: questo lavoro l'ha fatto
                 * Gurobi, quindi mi basta trovare quella xij[nowImHere][j] tale che
                 * la variabile binaria sia pari ad 1.
                 */
                if (xij[nowImHere][j] == 1) {
                    /*
                     * Questo frammento di codice svolge un'operazione fondamentale. Gurobi
                     * ha stabilito che fra il vertice nowImHere e j c'è un percorso che è il migliore,
                     * quindi lo aggiungo al mio arraylist che salva tutti i nodi visitati in
                     * ordine di passaggio. Il commesso viaggiatore parte da zero, quindi la mia
                     * variabile nowImHere è settata a zero. Proseguendo, però, chiaramente
                     * il commesso viaggia e si sposta in un altro nodo, quindi nowImHere
                     * va aggiornata, ma con cosa? Siccome ho stabilito nell'if che il link è
                     * attivo, allora il commesso si è spostato in j, quindi nowImHere = j
                     * e prosegue il suo percorso, incrementando nuovamente i e trovando dove
                     * si sposterà successivamente, così fino a che il percorso non viene concluso
                     * e il commesso viaggiatore si trova nuovamente in nowImHere = 0,
                     * concludendo il ciclo Hamiltoniano.
                     */
                    optimumPath.add(j);
                    nowImHere = j;
                    break; // mi serve per uscire dal for e proseguire con il for esterno
                }
                //continue; non va bene, continua il for annidato!!!
            }
    }

    /**
     * Le variabili xij gurobi le salva come array. Quindi,
     * quando ho eseguito il comando model.set(GRB.IntParam.SolutionNumber, i),
     * ho avuto la necessità di ritornare alla forma matriciale,
     * in modo tale da poi trovare quale fosse il valore nella riga
     * avente valore pari ad 1.
     *
     * @param x array di double.
     * @return la matrice dei link attivi.
     */
    private static double[][] conversion(double[] x) {
        double array2d[][] = new double[MATRIX_SIZE][MATRIX_SIZE];

        for(int i = 0; i < MATRIX_SIZE; i++)
            for(int j = 0; j < MATRIX_SIZE; j++)
                array2d[i][j] = x[(j*MATRIX_SIZE) + i];

        return array2d;
    }
}


