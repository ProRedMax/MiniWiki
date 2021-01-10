package wiki.mini.version.control;


class MWVCTest {

    @org.junit.jupiter.api.Test
    void createOldLastVersion() {
        MWVC.readVersions("C:\\Users\\mabug\\Desktop\\test.mw");
        System.out.println(MWVC.createOldLastVersion());
    }
}