package Controller;

import Exceptions.MyException;
import Model.ADT.MyIHeap;
import Model.PrgState;
import Model.Value.RefValue;
import Model.Value.Value;
import Repository.IRepo;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller {
    private IRepo repo;
    private ExecutorService executor;
    //represents an asynchronous execution mechanism which is capable of executing tasks in the background

    public Controller(IRepo repo) {
        this.repo = repo;
    }

    public IRepo getRepo(){return this.repo;}

    // In general, the garbage collector removes those addresses which are not referred from the SymTable and from other Heap table entries
    private Map<Integer, Value> GarbageCollector(List<Integer> symTableAddr, ConcurrentHashMap<Integer, Value> heap, List<Integer> heapAddr) {
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()) || heapAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<Integer> getAddrFromHeap(Collection<Value> heapValues) {
        return heapValues.stream().
                filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddr();
                }).
                collect(Collectors.toList());
    }

    private List<Integer> getAddrFromSymTable(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddr();
                })
                .collect(Collectors.toList());
    }

    //executes one step for each existing PrgState (namely each thread),
    public void oneStepForAllPrg(List<PrgState> prgList) throws InterruptedException {
        //before the execution, print the PrgState List into the log file
        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        });
        //RUN concurrently one step for each of the existing PrgStates
        // -----------------------------------------------------------------------
        // prepare the list of callables
        List<Callable<PrgState>> callList = prgList.stream()
                .map((PrgState p) -> (Callable<PrgState>) (p::oneStep))
                .collect(Collectors.toList());
        //start the execution of the callables
        // it returns the list of new created PrgStates (namely threads)
        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        if (e.getCause() instanceof MyException)
                            System.out.println(e.getMessage());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        //add the new created threads to the list of existing threads
        prgList.addAll(newPrgList);
        //------------------------------------------------------------------------------
        // after the execution, print the PrgState List into the log file
        prgList.forEach(prg -> {
            try {
                repo.logPrgStateExec(prg);
            } catch (MyException e) {
                System.out.println(e.getMessage());
            }
        });
        //Save the current programs in the repository
        repo.setLst(prgList);
    }

    public void allStep() throws InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        //remove the completed programs
        List<PrgState> prgList = removeCompletedPrg(repo.getLst());
        while (prgList.size() > 0) {
            //HERE you can call conservativeGarbageCollector
            List<Integer> symbAddr = this.repo.getLst().stream()
                    .map(e -> this.getAddrFromSymTable(e.getSymTable().getContent().values()))
                    .reduce(new ArrayList<>(), (e1, e2) -> Stream.concat(e1.stream(), e2.stream())
                            .distinct().collect(Collectors.toList()));
            MyIHeap<Integer, Value> heap = this.repo.getLst().get(0).getHeap();
            List<Integer> heapAddr = this.getAddrFromHeap(heap.getContent().values());
            Map<Integer, Value> newHeap = GarbageCollector(symbAddr, heap.getContent(), heapAddr);
            heap.setContent(newHeap);

            oneStepForAllPrg(prgList);
            //remove the completed programs
            prgList = removeCompletedPrg(repo.getLst());
        }
        executor.shutdownNow();
        //HERE the repository still contains at least one Completed Prg and its List<PrgState> is not empty.
        // Note that oneStepForAllPrg calls the method setPrgList of repository in order to change the repository
        // update the repository state
        repo.setLst(prgList);
    }

    public void oneStep() throws InterruptedException {
        executor = Executors.newFixedThreadPool(2);
        //remove the completed programs
        List<PrgState> prgList = removeCompletedPrg(repo.getLst());
        if (prgList.size()>0) {
            //HERE you can call conservativeGarbageCollector
            List<Integer> symbAddr = this.repo.getLst().stream()
                    .map(e -> this.getAddrFromSymTable(e.getSymTable().getContent().values()))
                    .reduce(new ArrayList<>(), (e1, e2) -> Stream.concat(e1.stream(), e2.stream())
                            .distinct().collect(Collectors.toList()));
            MyIHeap<Integer, Value> heap = this.repo.getLst().get(0).getHeap();
            List<Integer> heapAddr = this.getAddrFromHeap(heap.getContent().values());
            Map<Integer, Value> newHeap = GarbageCollector(symbAddr, heap.getContent(), heapAddr);
            heap.setContent(newHeap);

            oneStepForAllPrg(prgList);
            //remove the completed programs
            prgList = removeCompletedPrg(repo.getLst());
        }
        executor.shutdownNow();
        //HERE the repository still contains at least one Completed Prg and its List<PrgState> is not empty.
        // Note that oneStepForAllPrg calls the method setPrgList of repository in order to change the repository
        // update the repository state
        repo.setLst(prgList);
    }

    //removes all PrgState for which isNotCompleted returns false and then returns as result a list where all PrgState are not completed.
    private List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(p -> p.isNotCompleted())
                .collect(Collectors.toList());
    }
}
