package info.seltenheim.homepage.services.positions;

import info.seltenheim.homepage.services.database.Education;
import info.seltenheim.homepage.services.database.Employment;
import info.seltenheim.homepage.services.database.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("positionsMongo")
public class PositionsServiceMongoDb implements PositionsService {
    private final EmploymentMapper employmentMapper = new EmploymentMapper();
    private final EducationMapper educationMapper = new EducationMapper();

    @Override
    public List<Education> findAllEducations() throws IOException {
        return educationMapper.findAll();
    }

    @Override
    public List<Employment> findAllEmployments() throws IOException {
        return employmentMapper.findAll();
    }

    @Override
    public List<Position> findCurrentPositions() throws IOException {
        List<Position> positions = new ArrayList<Position>();
        positions.addAll(employmentMapper.findCurrentEmployments());
        positions.addAll(educationMapper.findCurrentEducations());
        return positions;
    }

    @Override
    public Education findEducationById(String educationId) throws IOException {
        return educationMapper.findById(educationId);
    }

    @Override
    public Employment findEmploymentById(String employmentId) throws IOException {
        return employmentMapper.findById(employmentId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Position> T upsertPosition(T position) throws IOException {

        if (position instanceof Employment) {
            return (T) employmentMapper.upsert((Employment) position);
        } else if (position instanceof Education) {
            return (T) educationMapper.upsert((Education) position);
        } else {
            throw new AssertionError("should not be reached");
        }
    }
}
