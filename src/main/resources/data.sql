-- DML
INSERT INTO training_type (training_type) VALUES ('CARDIO') ON CONFLICT DO NOTHING;
INSERT INTO training_type (training_type) VALUES ('STRENGTH') ON CONFLICT DO NOTHING;
INSERT INTO training_type (training_type) VALUES ('FLEXIBILITY') ON CONFLICT DO NOTHING;
INSERT INTO training_type (training_type) VALUES ('BALANCE') ON CONFLICT DO NOTHING;