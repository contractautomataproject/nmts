package io.github.contractautomata.nmts;

import io.github.contractautomata.catlib.automaton.Automaton;
import io.github.contractautomata.catlib.automaton.label.Label;
import io.github.contractautomata.catlib.automaton.label.action.Action;
import io.github.contractautomata.catlib.automaton.state.State;
import io.github.contractautomata.catlib.automaton.transition.ModalTransition;
import io.github.contractautomata.catlib.operations.ModelCheckingFunction;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class NMTSRefinement {
    public static Set<Set<State<String>>> computeRCS(Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> mts) {

        ModelCheckingFunction<String, State<String>, Label<Action>,
                ModalTransition<String, Action, State<String>, Label<Action>>, Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>>> mcf =
                new ModelCheckingFunction<>(mts, mts, State::new, ModalTransition::new, Label::new, Automaton::new);
        Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> synchProd = mcf.apply(Integer.MAX_VALUE);

        Set<Set<State<String>>> reachabilityClosedSets = new HashSet<>();
        Set<State<String>> L = new HashSet<>();


        for (State<String> state : mts.getStates()) {
            if (!L.contains(state)) {
                Set<State<String>> reachabilityClosedSet = new HashSet<>();
                reachabilityClosedSet.add(state);
                L.add(state);
                Set<State<String>> toExplore = new HashSet<>();
                toExplore.add(state);


                while (!toExplore.isEmpty()) {
                    State<String> currentState = toExplore.iterator().next();
                    toExplore.remove(currentState);


                    for (State<String> otherState : mts.getStates()) {
                        if (!reachabilityClosedSet.contains(otherState) && contains(synchProd.getStates(), currentState, otherState)) {
                            reachabilityClosedSet.add(otherState);
                            L.add(otherState);
                            toExplore.add(otherState);
                        }
                    }
                }
                reachabilityClosedSets.add(reachabilityClosedSet);
            }
        }
        return reachabilityClosedSets;
    }

    private static boolean contains(Set<State<String>> set, State<String> currentState, State<String> otherState) {
        return set.parallelStream()
                .anyMatch(s -> s.getState().get(0).equals(
                        currentState.getState().get(0)) &&
                        s.getState().get(1).equals(
                                otherState.getState().get(0)));
    }

    public static boolean isNMTS(Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> mts) {
        Set<Set<State<String>>> reachabilityClosedSets = computeRCS(mts);

        for (Set<State<String>> rcs : reachabilityClosedSets) {
            for (State<String> state1 : rcs) {
                for (State<String> state2 : rcs) {
                    if (!state1.equals(state2)) {
                        for (ModalTransition<String, Action, State<String>, Label<Action>> transition1 : mts.getTransition()) {
                            if (transition1.getSource().equals(state1)) {
                                for (ModalTransition<String, Action, State<String>, Label<Action>> transition2 : mts.getTransition()) {
                                    if (transition2.getSource().equals(state2)
                                            && transition1.getLabel().getAction().equals(transition2.getLabel().getAction())
                                            && transition1.isNecessary() != transition2.isNecessary()) {
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }


    public static Set<State<String>> NMTSRefinement(Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> S, Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> T) {
        return computeNMTSRefinement(S,T,false);
    }

    public static Set<State<String>> NMTSRefinementIgnore(Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> S, Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> T) {
        System.out.println("Warning: NMTS Refinement is not checking whether the operands are NMTS or MTS.");
        return computeNMTSRefinement(S,T,true);
    }

    private static Set<State<String>> computeNMTSRefinement(Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> S, Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> T, boolean ignore) {

        if (!ignore && (!isNMTS(S) || !isNMTS(T))) {
            System.out.println("One of the MTS is not a NMTS");
            return null;
        }

        Set<Set<State<String>>> reachabilityClosedSetsS = computeRCS(S);
        Set<Set<State<String>>> reachabilityClosedSetsT = computeRCS(T);

        ModelCheckingFunction<String, State<String>, Label<Action>,
                ModalTransition<String, Action, State<String>, Label<Action>>, Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>>> mcf =
                new ModelCheckingFunction<>(S, T, State::new, ModalTransition::new, Label::new, Automaton::new);
        Automaton<String, Action, State<String>, ModalTransition<String, Action, State<String>, Label<Action>>> synchProd = mcf.apply(Integer.MAX_VALUE);

        Set<State<String>> R = synchProd.getStates();
        Set<State<String>> toRemove = new HashSet<>();
        do {
            R.removeAll(toRemove);
            toRemove = new HashSet<>();
            for (State<String> pair : R) {
                State<String> s = S.getStates().parallelStream()
                        .filter(si->pair.getState().get(0).equals(si.getState().get(0)))
                        .findFirst().orElseThrow();

                State<String> t = T.getStates().parallelStream()
                        .filter(si->pair.getState().get(1).equals(si.getState().get(0)))
                        .findFirst().orElseThrow();
                Set<Action> AT = T.getTransition()
                        .parallelStream()
                        .map(ti->ti.getLabel().getAction())
                        .collect(Collectors.toSet());

                if (!(conditionT(s,t,S.getTransition(),T.getTransition(),R,reachabilityClosedSetsS) &&
                        conditionS(s,t,S.getTransition(),T.getTransition(),R) &&
                        condition4(s,t,AT,reachabilityClosedSetsS,reachabilityClosedSetsT,S.getTransition(),T.getTransition()))){
                    toRemove.add(pair);
                }

            }
        } while (!toRemove.isEmpty());
        return R;
    }

    private static boolean conditionT(State<String> s, State<String> t,
                               Set<ModalTransition<String, Action, State<String>, Label<Action>>> DeltaS,
                               Set<ModalTransition<String, Action, State<String>, Label<Action>>> DeltaT,
                               Set<State<String>> R,
                               Set<Set<State<String>>> reachabilityClosedSetsS) {
        for (ModalTransition<String, Action, State<String>, Label<Action>> deltaT : DeltaT) {
            if (deltaT.getSource().equals(t)) {
                boolean found = false;
                for (ModalTransition<String, Action, State<String>, Label<Action>> deltaS : DeltaS) {
                    if (deltaS.getSource().equals(s)
                            && deltaS.getLabel().getAction().equals(deltaT.getLabel().getAction())
                            && contains(R, deltaS.getTarget(), deltaT.getTarget())
                            && (deltaS.isNecessary() || deltaT.isPermitted()))
                        found = true;
                }
                if (!found && deltaT.isNecessary()) return false;
                else if (!found) {
                    for (Set<State<String>> Ci : reachabilityClosedSetsS) {
                        if (Ci.contains(s)) {
                            for (State<String> si : Ci) {
                                for (ModalTransition<String, Action, State<String>, Label<Action>> deltaS : DeltaS) {
                                    if (deltaS.getSource().equals(si)
                                            && deltaS.getLabel().getAction().equals(deltaT.getLabel().getAction()))
                                        return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private static boolean conditionS(State<String> s, State<String> t,
                               Set<ModalTransition<String, Action, State<String>, Label<Action>>> DeltaS,
                               Set<ModalTransition<String, Action, State<String>, Label<Action>>> DeltaT,
                               Set<State<String>> R) {
        for (ModalTransition<String, Action, State<String>, Label<Action>> deltaS : DeltaS) {
            if (deltaS.getSource().equals(s)) {
                boolean found = false;
                for (ModalTransition<String, Action, State<String>, Label<Action>> deltaT : DeltaT) {
                    if (deltaT.getSource().equals(t)
                            && deltaS.getLabel().getAction().equals(deltaT.getLabel().getAction())
                            && contains(R, deltaS.getTarget(), deltaT.getTarget()))
                        found = true;
                }
                if (!found) return false;
            }
        }
        return true;
    }


    private static boolean condition4(State<String> s, State<String> t,
                               Set<Action> AT,
                               Set<Set<State<String>>> reachabilityClosedSetsS,
                               Set<Set<State<String>>> reachabilityClosedSetsT,
                               Set<ModalTransition<String, Action, State<String>, Label<Action>>> DeltaS,
                               Set<ModalTransition<String, Action, State<String>, Label<Action>>> DeltaT){
        for (Action a: AT){
            for (Set<State<String>> Ci : reachabilityClosedSetsT){
                if (Ci.contains(t)){
                    boolean nexists=true;
                    for (ModalTransition<String, Action, State<String>, Label<Action>> deltaT : DeltaT){
                        if (Ci.contains(deltaT.getSource()) &&
                        deltaT.getLabel().getAction().equals(a))
                            nexists=false;
                    }
                    if (nexists){
                        for (Set<State<String>> Cj : reachabilityClosedSetsS){
                            if (Cj.contains(s)){
                                for (ModalTransition<String, Action, State<String>, Label<Action>> deltaS : DeltaS){
                                    if (Cj.contains(deltaS.getSource()) &&
                                            deltaS.getLabel().getAction().equals(a)){
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

}

