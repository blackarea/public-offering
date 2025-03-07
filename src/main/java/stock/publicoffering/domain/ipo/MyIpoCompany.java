package stock.publicoffering.domain.ipo;

public enum MyIpoCompany {
    NH, KB, 미래, 삼성, 한국;

    public static boolean contains(String company) {
        for (MyIpoCompany c : MyIpoCompany.values()) {
            if (company.contains(c.name())) {
                return true;
            }
        }
        return false;
    }
}
