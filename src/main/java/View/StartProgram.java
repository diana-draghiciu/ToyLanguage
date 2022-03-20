package View;

import Model.Expression.*;
import Model.Statement.*;
import Model.Type.BoolType;
import Model.Type.IntType;
import Model.Type.RefType;
import Model.Type.StringType;
import Model.Value.BoolValue;
import Model.Value.IntValue;
import Model.Value.StringValue;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class StartProgram extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartProgram.class.getResource("select_program.fxml"));
        fxmlLoader.setController(new SelectProgram(getStatements()));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        stage.show();
    }

    private ArrayList<IStmt> getStatements(){
        ArrayList<IStmt> programs = new ArrayList<>();

        IStmt ex1 = new CompStmt(new VarDeclStmt("v", new BoolType()), new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(2))), new PrintStmt(new VarExp("v"))));

        //int a;
        //int b;
        //a = 2 + 3 * 5;
        //b = a + 1;
        //Print(b) is represented as:
        IStmt ex2 = new CompStmt(new VarDeclStmt("a", new IntType()), new CompStmt(new VarDeclStmt("b", new IntType()), new CompStmt(new AssignStmt("a", new ArithExp(new ValueExp(new IntValue(2)), new ArithExp(new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5)), "*"), "+")), new CompStmt(new AssignStmt("b", new ArithExp(new VarExp("a"), new ValueExp(new IntValue(1)), "+")), new PrintStmt(new VarExp("b"))))));

        //boolean a;
        //a = true;
        //(If a Then v = 2 Else v = 3);
        //Print(v) is represented as
        IStmt ex3 = new CompStmt(new VarDeclStmt("a", new BoolType()), new CompStmt(new VarDeclStmt("v", new IntType()), new CompStmt(new AssignStmt("a", new ValueExp(new BoolValue(true))), new CompStmt(new IfStmt(new VarExp("a"), new AssignStmt("v", new ValueExp(new IntValue(2))), new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new VarExp("v"))))));

        //String varf;
        //varf="test.in";
        //openRFile(varf);
        //int varc;
        //readFile(varf,varc);print(varc);
        //readFile(varf,varc);print(varc);
        //closeRFile(varf);
        IStmt ex4 = new CompStmt(new VarDeclStmt("varf", new StringType()),
                new CompStmt(new VarDeclStmt("varc", new IntType()),
                        new CompStmt(new AssignStmt("varf", new ValueExp(new StringValue("test.in"))),
                                new CompStmt(new OpenRFile(new VarExp("varf")),
                                        new CompStmt(new ReadFile("varc", new VarExp("varf")), new CompStmt(new PrintStmt(new VarExp("varc")),
                                                new CompStmt(new ReadFile("varc", new VarExp("varf")), new CompStmt(new PrintStmt(new VarExp("varc")),
                                                        new CloseRFile(new VarExp("varf"))))))))));
        // Ref int v;
        // new(v,20);
        // Ref Ref int a;
        // new(a,v);
        // print(v);print(a)
        // At the end of execution:
        // Heap={1->20, 2->(1,int)}, SymTable={v->(1,int), a->(2,Ref int)} and Out={(1,int),(2,Ref int)}
        IStmt ex5 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewHeapStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewHeapStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new VarExp("a")))))));
        // Ref int v;
        // new(v,20);
        // Ref Ref int a;
        // new(a,v);
        // print(rH(v));print(rH(rH(a))+5)
        // At the end of execution:  Heap={1->20, 2->(1,int)}, SymTable={v->(1,int), a->(2,Ref int)} and Out={20, 25}
        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewHeapStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewHeapStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new rH(new VarExp("v"))), new PrintStmt(new rH(new VarExp("a"))))))));
        //Ref int v;new(v,20);print(rH(v)); wH(v,30);print(rH(v)+5);
        // At the end of execution:  Heap={1->30}, SymTable={v->(1,int)} and Out={20, 35}
        IStmt ex7 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewHeapStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new PrintStmt(new rH(new VarExp("v"))),
                                new CompStmt(new wH("v", new ValueExp(new IntValue(30))), new PrintStmt(new ArithExp(new rH(new VarExp("v")), new ValueExp(new IntValue(5)), "+"))))));

        //Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))
        IStmt ex8 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewHeapStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewHeapStmt("a", new VarExp("v")),
                                        new CompStmt(new NewHeapStmt("v", new ValueExp(new IntValue(30))), new PrintStmt(new rH(new rH(new VarExp("a")))))))));

        // int v; v=4; (while (v>0) print(v);v=v-1);print(v)
        IStmt ex9 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(4))),
                        new CompStmt(new WhileStmt(
                                new RelatExp(new VarExp("v"), new ValueExp(new IntValue(0)), ">"),
                                new CompStmt(new PrintStmt(new VarExp("v")), new AssignStmt("v", new ArithExp(new VarExp("v"), new ValueExp(new IntValue(1)), "-")))),
                                new PrintStmt(new VarExp("v")))));
        //  int v;
        // Ref int a;
        // v=10;
        // new(a,22);
        // fork(wH(a,30); v=32; print(v); print(rH(a)));
        // print(v);
        // print(rH(a))
        IStmt ex10 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(new NewHeapStmt("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(new ForkStmt(new CompStmt(new wH("a", new ValueExp(new IntValue(30))), new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(32))), new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new rH(new VarExp("a"))))))),
                                                new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new rH(new VarExp("a")))))))));

        //Ref int v1; int cnt;
        //new(v1,1);createSemaphore(cnt,rH(v1));
        //fork(acquire(cnt);wh(v1,rh(v1)*10);print(rh(v1));release(cnt));
        //fork(acquire(cnt);wh(v1,rh(v1)*10);wh(v1,rh(v1)*2);print(rh(v1));release(cnt));
        //acquire(cnt);
        //print(rh(v1)-1);
        //release(cnt)
        //The final Out should be {10,200,9} or {10,9,200} or {10,200,199}


        IStmt exSemaphore=new CompStmt(new VarDeclStmt("v1",new RefType(new IntType())),
                new CompStmt(new VarDeclStmt("cnt",new IntType()),
                        new CompStmt(new NewHeapStmt("v1",new ValueExp(new IntValue(1))),
                                new CompStmt(new NewSemaphore("cnt", new rH(new VarExp("v1"))),
                                        new CompStmt(new ForkStmt(new CompStmt(new AquireStmt("cnt"),
                                                new CompStmt(new wH("v1",new ArithExp(new rH(new VarExp("v1")),
                                                        new ValueExp(new IntValue(10)),"*")),
                                                        new CompStmt(new PrintStmt(new rH(new VarExp("v1"))),
                                                                new ReleaseStmt("cnt"))))),
                                                new CompStmt(new ForkStmt(new CompStmt(new AquireStmt("cnt"),
                                                        new CompStmt(new wH("v1",new ArithExp(new rH(new VarExp("v1")),
                                                                new ValueExp(new IntValue(10)),"*")),
                                                                new CompStmt(new wH("v1",new ArithExp(new rH(new VarExp("v1")),
                                                                        new ValueExp(new IntValue(2)),"*")),
                                                                        new CompStmt(new PrintStmt(new rH(new VarExp("v1"))),
                                                                                new ReleaseStmt("cnt")))))),
                                                        new CompStmt(new AquireStmt("cnt"),
                                                                new CompStmt(new PrintStmt(new ArithExp(new rH(new VarExp("v1")),
                                                                        new ValueExp(new IntValue(1)),"-")), new ReleaseStmt("cnt")))))))));


        //int a; int b; int c;
        // a=1;b=2;c=5;
        // (switch(a*10)  (case (b*c) : print(a);print(b))  (case (10) : print(100);print(200))  (default : print(300)));print(300)
        // The final Out should be {1,2,300}

        IStmt exSwitch=new CompStmt(new VarDeclStmt("a", new IntType()), new CompStmt(new VarDeclStmt("b", new IntType()), new CompStmt(new VarDeclStmt("c", new IntType()),
                new CompStmt(new AssignStmt("a", new ValueExp(new IntValue(1))), new CompStmt(new AssignStmt("b", new ValueExp(new IntValue(2))), new CompStmt(new AssignStmt("c",new ValueExp(new IntValue(5))),
                        new CompStmt(new SwitchStmt(new ArithExp(new VarExp("a"), new ValueExp(new IntValue(10)), "*"), new ArithExp(new VarExp("b"), new VarExp("c"), "*"), new ValueExp(new IntValue(10)),
                                new CompStmt(new PrintStmt(new VarExp("a")), new PrintStmt(new VarExp("b"))), new CompStmt(new PrintStmt(new ValueExp(new IntValue(100))),new PrintStmt(new ValueExp(new IntValue(200)))),
                                new CompStmt(new PrintStmt(new ValueExp(new IntValue(300))), new PrintStmt(new ValueExp(new IntValue(300))))),new PrintStmt(new ValueExp(new IntValue(300))))))))));

        programs.add(ex1);
        programs.add(ex2);
        programs.add(ex3);
        programs.add(ex4);
        programs.add(ex5);
        programs.add(ex6);
        programs.add(ex7);
        programs.add(ex8);
        programs.add(ex9);
        programs.add(ex10);
        programs.add(exSemaphore);
        programs.add(exSwitch);
        return programs;
    }

    public static void main(String[] args) {
        launch();
    }
}