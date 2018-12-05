public class AggroTableModel extends EntityTableModel<Aggro> {

    public final Attribute<String> WASTE_TYPE = new Attribute<>("Waste Type",
    String.class, Aggro::getWaste_type);

    public final MutableAttribute<Double> SUM = new MutableAttribute<>("Sum",
    Double.class, Aggro::getSum, Aggro::setSum);

    public final MutableAttribute<Double> AVERAGE = new MutableAttribute<>("Average",
    Double.class, Aggro::getAverage, Aggro::setAverage);

    public AggroTableModel() {
        setColumns(WASTE_TYPE, SUM, AVERAGE);
    }
}