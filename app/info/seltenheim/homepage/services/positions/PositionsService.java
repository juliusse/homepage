package info.seltenheim.homepage.services.positions;

import info.seltenheim.homepage.services.database.Education;
import info.seltenheim.homepage.services.database.Employment;
import info.seltenheim.homepage.services.database.Position;

import java.io.IOException;
import java.util.List;

public interface PositionsService {

    List<Education> findAllEducations() throws IOException;

    List<Employment> findAllEmployments() throws IOException;

    List<Position> findCurrentPositions() throws IOException;

    Education findEducationById(String educationId) throws IOException;

    Employment findEmploymentById(String employmentId) throws IOException;

    <T extends Position> T upsertPosition(T position) throws IOException;

}
