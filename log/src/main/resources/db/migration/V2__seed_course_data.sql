INSERT INTO public.course (id, title) VALUES (1, 'Desenvolvimento Android Nativo com Kotlin') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (2, 'Arquitetura de Microservicos com Spring Boot') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (3, 'Reparo Avancado de Placas-Mae de Smartphones') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (4, 'Administracao de Sistemas Debian Linux') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (5, 'Testes Unitarios e Mockito para Desenvolvedores Java') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (6, 'Eletronica Digital e Microcontroladores Arduino') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (7, 'Otimizacao de Consultas em MongoDB Aggregation Framework') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (8, 'Manutencao e Reballing de GPUs') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (9, 'Seguranca em APIs Bancarias') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (10, 'Design de Interfaces (UI/UX) para Mobile') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (11, 'Docker e Orquestracao de Containers') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (12, 'Analise de Circuitos de Potencia e Fontes Chaveadas') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (13, 'Desenvolvimento de Jogos com C# e Unity') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (14, 'Redes de Computadores e Protocolos TCP/IP') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (15, 'Inteligencia Artificial Aplicada a Sistemas Embarcados') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (16, 'Gestao de Projetos Ageis com Scrum e Kanban') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (17, 'Soldagem SMD e Retrabalho de Componentes BGA') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (18, 'Programacao Funcional em TypeScript') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (19, 'Fundamentos de Cloud Computing com AWS') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (20, 'Instrumentacao Eletronica: Osciloscopio e Multimetro') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (21, 'Desenvolvimento Fullstack com Next.js e Prisma') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (22, 'Sistemas Operacionais: Kernel e Gerenciamento de Memoria') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (23, 'Arquitetura Clean e Hexagonal em Projetos Java') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (24, 'Manutencao de Hardware de Alta Performance (Workstations)') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (25, 'Criptografia e Protecao de Dados (LGPD)') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (26, 'Automacao Residencial com ESP32') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (27, 'Tecnicas de Diagnostico Termico em PCBs') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (28, 'DevOps: Pipelines CI/CD com GitHub Actions') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (29, 'Ingles Tecnico para Profissionais de TI') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;
INSERT INTO public.course (id, title) VALUES (30, 'Lideranca e Gestao de Equipes de Engenharia') ON CONFLICT (id) DO UPDATE SET title = EXCLUDED.title;

SELECT setval(
  pg_get_serial_sequence('public.course', 'id'),
  COALESCE((SELECT MAX(id) FROM public.course), 1),
  true
);

