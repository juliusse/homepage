package info.seltenheim.homepage.services.positions;


import java.io.IOException;
import java.util.List;

import com.google.inject.ImplementedBy;

@ImplementedBy(PositionsServiceMongoDb.class)
public interface PositionsService {

    List<Education> findAllEducations() throws IOException;

    List<Employment> findAllEmployments() throws IOException;

    List<Position> findCurrentPositions() throws IOException;

    Education findEducationById(String educationId) throws IOException;

    Employment findEmploymentById(String employmentId) throws IOException;

    <T extends Position> T upsertPosition(T position) throws IOException;

}
