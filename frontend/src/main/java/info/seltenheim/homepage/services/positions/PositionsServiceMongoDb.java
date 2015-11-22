package info.seltenheim.homepage.services.positions;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PositionsServiceMongoDb implements PositionsService {
    private final EmploymentMapper employmentMapper;
    private final EducationMapper educationMapper;

    @Inject
    public PositionsServiceMongoDb(EmploymentMapper employmentMapper, EducationMapper educationMapper) {
        this.employmentMapper = employmentMapper;
        this.educationMapper = educationMapper;
    }

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
