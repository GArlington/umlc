package umlc.codeGeneration;


public class GeneratorInfo {

    public String class_name;
    public String output_dir;

    public GeneratorInfo (String _class_name, String _output_dir) {
	class_name = _class_name;
	output_dir = _output_dir;
    }
}
