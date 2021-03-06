package seedu.inventory.logic.parser;

import static seedu.inventory.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.inventory.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.inventory.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Ignore;
import org.junit.Test;

import seedu.inventory.logic.commands.csv.ExportCsvCommand;
import seedu.inventory.logic.parser.csv.ExportCsvCommandParser;

public class ExportCsvCommandParserTest {
    private ExportCsvCommandParser parser = new ExportCsvCommandParser();

    @Test
    public void parse_validPath_success() throws Exception {
        Path expectedPath = Paths.get("valid.csv");
        parser.parse(" f/valid.csv");
        assertParseSuccess(parser, " f/valid.csv", new ExportCsvCommand(expectedPath));
        expectedPath = Paths.get("valid/valid");
        assertParseSuccess(parser, " f/valid/valid", new ExportCsvCommand(expectedPath));
        expectedPath = Paths.get("valid/valid.jpg");
        assertParseSuccess(parser, " f/valid/valid.jpg", new ExportCsvCommand(expectedPath));
    }

    @Ignore
    @Test
    public void parse_invalidPath_failure() {
        assertParseFailure(parser, " f/ csv/:csv", ParserUtil.MESSAGE_FILEPATH_CONSTRAINTS);
    }

    @Test
    public void parse_invalidField_success() {
        assertParseFailure(parser, " d/valid.csv", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ExportCsvCommand.MESSAGE_USAGE));
    }
}
