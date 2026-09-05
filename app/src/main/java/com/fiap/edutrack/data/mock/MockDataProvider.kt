package com.fiap.edutrack.data.mock

import com.fiap.edutrack.data.model.*

/**
 * Fonte única de dados mockados do EduTrack.
 * Nesta Sprint (MVP), todos os dados abaixo simulam o que viria futuramente
 * da integração com o Moodle institucional e do backend de presença/gamificação.
 */
object MockDataProvider {

    val institutos = listOf(
        Instituto("senai", "SENAI", "SENAI", "Indústria"),
        Instituto("senac", "SENAC", "SENAC", "Comércio"),
        Instituto("alicerce", "Instituto Alicerce", "Alicerce", "Reforço escolar"),
        Instituto("eurofarma", "Instituto Eurofarma", "Eurofarma", "Farmácia")
    )

    val alunoLogado = Aluno(
        nome = "Lucas Rodrigues",
        email = "lucas.rodrigues@aluno.br",
        matricula = "RM 00341",
        instituto = institutos.first { it.id == "senai" },
        anoEgresso = 2026,
        nivel = 7,
        xpAtual = 1240,
        xpProximoNivel = 2000,
        streakDias = 15,
        tituloNivel = "Explorador"
    )

    val perguntasOnboarding = listOf(
        PerguntaOnboarding(
            id = 1,
            texto = "Em uma tarde livre, o que mais te empolga?",
            opcoes = listOf(
                OpcaoOnboarding("A", "Liderar um projeto e organizar a equipe", PerfilHolland.EMPREENDEDOR),
                OpcaoOnboarding("B", "Resolver um problema lógico ou de cálculo", PerfilHolland.INVESTIGATIVO),
                OpcaoOnboarding("C", "Criar um conteúdo ou ilustração", PerfilHolland.ARTISTICO),
                OpcaoOnboarding("D", "Cuidar e orientar outras pessoas", PerfilHolland.SOCIAL)
            )
        ),
        PerguntaOnboarding(
            id = 2,
            texto = "Qual dessas tarefas você faria de bom grado, sem ninguém pedir?",
            opcoes = listOf(
                OpcaoOnboarding("A", "Consertar ou montar algo com as mãos", PerfilHolland.REALISTA),
                OpcaoOnboarding("B", "Organizar planilhas e processos", PerfilHolland.CONVENCIONAL),
                OpcaoOnboarding("C", "Apresentar uma ideia para um grupo", PerfilHolland.EMPREENDEDOR),
                OpcaoOnboarding("D", "Pesquisar a fundo sobre um assunto novo", PerfilHolland.INVESTIGATIVO)
            )
        ),
        PerguntaOnboarding(
            id = 3,
            texto = "Num trabalho em grupo, qual papel você assume naturalmente?",
            opcoes = listOf(
                OpcaoOnboarding("A", "Quem organiza prazos e entregas", PerfilHolland.CONVENCIONAL),
                OpcaoOnboarding("B", "Quem media conflitos e escuta todo mundo", PerfilHolland.SOCIAL),
                OpcaoOnboarding("C", "Quem propõe ideias fora da caixa", PerfilHolland.ARTISTICO),
                OpcaoOnboarding("D", "Quem coloca a mão na massa primeiro", PerfilHolland.REALISTA)
            )
        )
    )

    val aulas = listOf(
        Aula(
            id = "aula-01",
            titulo = "Lógica de Programação",
            instituto = "SENAI",
            horario = "14h–16h",
            data = "Hoje",
            descricao = "Encontro prático sobre estruturas de repetição. Traga os exercícios " +
                "da semana passada: a segunda metade da aula é resolução em dupla.",
            topicos = listOf(
                "Revisão: condicionais e operadores lógicos",
                "Laço while e laço for — quando usar cada um",
                "Contador e acumulador dentro de um laço",
                "Erro clássico: laço que nunca termina",
                "Exercício em dupla: somar os números de 1 a N"
            ),
            presencaConfirmada = true
        ),
        Aula(
            id = "aula-02",
            titulo = "Matemática Aplicada à Indústria",
            instituto = "SENAI",
            horario = "14h–16h",
            data = "Quinta, 4 set",
            descricao = "Como regra de três e porcentagem aparecem no chão de fábrica: " +
                "cálculo de produção, perdas e tempo de linha.",
            topicos = listOf(
                "Proporção e regra de três em ordens de produção",
                "Porcentagem de perda e de retrabalho",
                "Leitura de gráficos de produtividade",
                "Exercício: calcular a meta diária de uma linha"
            ),
            presencaConfirmada = false
        ),
        Aula(
            id = "aula-03",
            titulo = "Comunicação no Ambiente de Trabalho",
            instituto = "SENAC",
            horario = "9h–11h",
            data = "Sexta, 5 set",
            descricao = "Oficina de comunicação para quem está entrando no primeiro emprego: " +
                "como pedir ajuda, dar retorno e receber crítica sem travar.",
            topicos = listOf(
                "Escuta ativa: ouvir para entender, não para responder",
                "Como pedir ajuda sem parecer despreparado",
                "Dar e receber feedback",
                "Simulação: alinhar uma entrega atrasada com a liderança"
            ),
            presencaConfirmada = false
        )
    )

    /** A aula do dia é sempre a primeira da agenda — é ela que a Home destaca. */
    val aulaDeHoje = aulas.first()

    val trilhas = listOf(
        Trilha(
            id = "trilha-logica",
            nome = "Pensamento Algorítmico",
            area = AreaTrilha.LOGICA,
            moduloAtual = 3,
            totalModulos = 5,
            aulasRestantes = 4,
            progresso = 0.58f
        ),
        Trilha(
            id = "trilha-stem",
            nome = "Matemática Aplicada",
            area = AreaTrilha.STEM,
            moduloAtual = 2,
            totalModulos = 6,
            aulasRestantes = 6,
            progresso = 0.33f
        ),
        Trilha(
            id = "trilha-carreira",
            nome = "Primeiros Passos na Carreira",
            area = AreaTrilha.CARREIRA,
            moduloAtual = 1,
            totalModulos = 4,
            aulasRestantes = 3,
            progresso = 0.20f
        ),
        Trilha(
            id = "trilha-softskills",
            nome = "Comunicação e Trabalho em Equipe",
            area = AreaTrilha.SOFT_SKILLS,
            moduloAtual = 4,
            totalModulos = 4,
            aulasRestantes = 0,
            progresso = 1.0f
        )
    )

    val quizzes = listOf(
        Quiz(
            id = "quiz-logica-01",
            trilhaId = "trilha-logica",
            pergunta = "Se todo programador resolve problemas e Ana resolve problemas, então...",
            alternativas = listOf(
                "Ana é programadora",
                "Ana pode ser programadora, mas não necessariamente",
                "Ana nunca foi programadora",
                "Programadores não resolvem problemas"
            ),
            indiceCorreto = 1,
            xp = 30
        ),
        Quiz(
            id = "quiz-logica-02",
            trilhaId = "trilha-logica",
            pergunta = "Qual estrutura repete um bloco de código enquanto uma condição for verdadeira?",
            alternativas = listOf("if/else", "while", "var", "class"),
            indiceCorreto = 1,
            xp = 20
        ),
        Quiz(
            id = "quiz-stem-01",
            trilhaId = "trilha-stem",
            pergunta = "Qual é o resultado de 7 + 3 × 2?",
            alternativas = listOf("20", "13", "17", "10"),
            indiceCorreto = 1,
            xp = 20
        ),
        Quiz(
            id = "quiz-carreira-01",
            trilhaId = "trilha-carreira",
            pergunta = "Você ainda não teve emprego formal. O que colocar no currículo?",
            alternativas = listOf(
                "Deixar o currículo em branco até conseguir o primeiro emprego",
                "Cursos, projetos da escola, trabalho voluntário e atividades em equipe",
                "Inventar uma experiência curta para não parecer sem nada",
                "Só os dados pessoais, porque o resto a empresa pergunta na entrevista"
            ),
            indiceCorreto = 1,
            xp = 25
        ),
        Quiz(
            id = "quiz-carreira-02",
            trilhaId = "trilha-carreira",
            pergunta = "Numa entrevista, o recrutador pergunta um ponto fraco seu. Qual é a melhor resposta?",
            alternativas = listOf(
                "\"Não tenho nenhum ponto fraco\"",
                "Citar um defeito grave para parecer sincero",
                "Citar algo real e dizer o que você já faz para melhorar",
                "Falar de um ponto fraco de um colega de turma"
            ),
            indiceCorreto = 2,
            xp = 25
        ),
        Quiz(
            id = "quiz-softskills-01",
            trilhaId = "trilha-softskills",
            pergunta = "No trabalho em grupo, um colega explica uma ideia e você discorda. Qual atitude ajuda mais o grupo?",
            alternativas = listOf(
                "Interromper na hora para não perder o raciocínio",
                "Ouvir até o fim, repetir o que entendeu e então apresentar sua visão",
                "Ficar calado e reclamar depois, fora da reunião",
                "Aceitar a ideia mesmo discordando, para evitar atrito"
            ),
            indiceCorreto = 1,
            xp = 25
        ),
        Quiz(
            id = "quiz-softskills-02",
            trilhaId = "trilha-softskills",
            pergunta = "Sua dupla não entregou a parte dela e o prazo é amanhã. Qual é o primeiro passo?",
            alternativas = listOf(
                "Avisar o professor antes de falar com a dupla",
                "Refazer tudo sozinho e não comentar nada",
                "Conversar com a dupla, entender o que travou e combinar como fechar a entrega",
                "Entregar só a sua parte e deixar claro de quem foi a culpa"
            ),
            indiceCorreto = 2,
            xp = 30
        )
    )

    // As missões começam **em aberto**: quem as conclui é o uso do app, em runtime.
    // Antes, `missao-01` já vinha concluída enquanto a tela ainda pedia o check-in do dia.
    val missoesDaSemana = listOf(
        Missao("missao-01", "Check-in emocional", TipoMissao.CHECKIN, 50, 0, 1, concluida = false),
        Missao("missao-02", "Faça 3 quizzes", TipoMissao.QUIZ, 150, 2, 3, concluida = false),
        Missao("missao-03", "Assista 2 aulas completas", TipoMissao.AULA, 100, 1, 2, concluida = false),
        Missao("missao-04", "Ajude 3 colegas no fórum", TipoMissao.FORUM, 90, 0, 3, concluida = false)
    )

    val badges = listOf(
        Badge("badge-presenca", "Presença Perfeita", "30 dias seguidos sem faltar", conquistada = true),
        Badge("badge-quiz", "Quiz Master", "+90% de acerto nos quizzes", conquistada = true),
        Badge("badge-trilha", "Primeira Trilha", "Concluiu a primeira trilha completa", conquistada = true),
        // O aluno tem streak de 15 dias: o badge estava incoerente com o próprio dado.
        Badge("badge-engajado", "Engajado", "15 dias seguidos de acesso", conquistada = true),
        Badge("badge-mentor", "Mentor", "Ajudou 3 colegas na plataforma", conquistada = false),
        // Desbloqueado ao concluir o onboarding, não antes de o aluno respondê-lo.
        Badge("badge-vocacao", "Vocação Definida", "Concluiu o teste Holland", conquistada = false)
    )

    val certificados = listOf(
        Certificado("cert-01", "Lógica de Programação", "SENAI", 40, "jun 2026", publicado = false),
        Certificado("cert-02", "Excel Avançado", "SENAI", 20, "mar 2026", publicado = false)
    )

    val evolucaoVocacional = listOf(
        EvolucaoVocacional(PerfilHolland.REALISTA, 25),
        EvolucaoVocacional(PerfilHolland.INVESTIGATIVO, 15),
        EvolucaoVocacional(PerfilHolland.SOCIAL, -10)
    )

    val checkInsRecentes = listOf(
        CheckInRegistro(
            data = "Quarta, 21 mai",
            humor = Humor.BEM,
            cargaTarefas = CargaTarefas.ALGUMAS_PESAM,
            comentario = "Tô meio cansado essa semana, mas o quiz de lógica me animou"
        )
    )
}
