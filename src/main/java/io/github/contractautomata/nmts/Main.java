package io.github.contractautomata.nmts;

import io.github.contractautomata.catlib.automaton.Automaton;
import io.github.contractautomata.catlib.automaton.label.Label;
import io.github.contractautomata.catlib.automaton.label.action.Action;
import io.github.contractautomata.catlib.automaton.state.BasicState;
import io.github.contractautomata.catlib.automaton.state.State;
import io.github.contractautomata.catlib.automaton.transition.ModalTransition;
import io.github.contractautomata.catlib.converters.AutDataConverter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class Main {
    private static final AutDataConverter<Label<Action>> adc = new AutDataConverter<>(Label::new);
    // private static final String dir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;

    public static void main(String[] args) throws IOException {


        Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> S;
        Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> T;
        Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> I;
        Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> U;

        System.out.println("Figure 2:");
        // S = adc.importMSCA(dir + "Fig2_S.data");
        // T = adc.importMSCA(dir + "Fig2_T.data");
        S = loadFile("Fig2_S.data");
        T = loadFile("Fig2_T.data");
        printExample(S,T,true,"S","T");

        System.out.println("");
        System.out.println("Figure 4:");
        // S = adc.importMSCA(dir + "Fig4_S.data");
        // T = adc.importMSCA(dir + "Fig4_T.data");
        S = loadFile("Fig4_S.data");
        T = loadFile("Fig4_T.data");
        printExample(S,T,false,"S","T");


        // I = adc.importMSCA(dir + "Fig4_I.data");
        I = loadFile("Fig4_I.data");
        System.out.println("");
        printExample(I,S,false,"I","S");

        System.out.println("");
        printExample(I,T,false,"I","T");

        System.out.println("");
        System.out.println("Figure 6:");
        // S = adc.importMSCA(dir + "Fig6_S.data");
        // T = adc.importMSCA(dir + "Fig6_T.data");
        S = loadFile("Fig6_S.data");
        T = loadFile("Fig6_T.data");

        printExample(S,T,true,"S","T");

        System.out.println("");
        System.out.println("Figure 7:");
        // S = adc.importMSCA(dir + "Fig7_S.data");
        // T = adc.importMSCA(dir + "Fig7_T.data");
        S = loadFile("Fig7_S.data");
        T = loadFile("Fig7_T.data");

        printExample(S,T,false,"S","T");


        printExample(T,S,false,"T","S");


        System.out.println("");
        System.out.println("Figure 8:");
        // S = adc.importMSCA(dir + "Fig8_S.data");
        // T = adc.importMSCA(dir + "Fig8_T.data");
        // U = adc.importMSCA(dir + "Fig8_U.data");
        S = loadFile("Fig8_S.data");
        T = loadFile("Fig8_T.data");
        U = loadFile("Fig8_U.data");

        printExample(S,T,false,"S","T");
        printExample(T,U,false,"T","U");


    }

    private static boolean isNMTSRefinement( Set<State<String>> R, Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> S, Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> T){
        BasicState<String> initS = S.getInitial().getState().get(0);
        BasicState<String> initT = T.getInitial().getState().get(0);
        return R.stream().anyMatch(p->p.getState().get(0).equals(initS)&&p.getState().get(1).equals(initT));
    }

    private static void printExample(Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>>  S, Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>>  T, boolean ignore, String Sname, String Tname){
        System.out.println("Reachability Closed sets of "+Sname+": "+ NMTSRefinement.computeRCS(S));

        System.out.println(Sname+" is an NMTS: "+ NMTSRefinement.isNMTS(S));

        System.out.println("Reachability Closed sets of "+Tname+":" + NMTSRefinement.computeRCS(T));

        System.out.println(Tname+" is an NMTS: "+ NMTSRefinement.isNMTS(T));

        Set<State<String>> R = null;
        if (ignore)
            R = NMTSRefinement.NMTSRefinementIgnore(S,T);
        else R = NMTSRefinement.NMTSRefinement(S,T);
        System.out.println(Sname+" <n "+Tname+" : "+isNMTSRefinement(R,S,T));
        if (isNMTSRefinement(R,S,T))
            System.out.println("R_("+Sname+" <n "+Tname+") : "+R);
    }

    private static Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> loadFile(String filename) throws IOException {
        InputStream in = Main.class.getClassLoader().getResourceAsStream(filename);
        File f = new File(filename);
        FileUtils.copyInputStreamToFile(in, f);

        Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>>  aut = adc.importMSCA(filename);
        f.delete();
        return aut;
    }

}