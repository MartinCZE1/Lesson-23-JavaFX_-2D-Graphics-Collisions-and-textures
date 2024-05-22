module cz.spsmb.sec.lesson22javafx_2dgraphicsbasics {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens cz.spsmb.sec.lesson22javafx_2dgraphicsbasics to javafx.fxml;
    exports cz.spsmb.sec.lesson22javafx_2dgraphicsbasics;
}