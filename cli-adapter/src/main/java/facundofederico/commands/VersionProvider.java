package facundofederico.commands;

import picocli.CommandLine;

public class VersionProvider implements CommandLine.IVersionProvider {
    @Override
    public String[] getVersion() {
        return new String[] { "ProjectManager version 1.0-SNAPSHOT" };
    }
}
