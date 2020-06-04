package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.seletor.ClienteSeletor;
import model.seletor.FuncionarioSeletor;
import model.vo.Cliente;
import model.vo.Funcionario;

public class FuncionarioDAO {

	public Funcionario cadastrar(Funcionario novoFuncionario) {
		Connection conn = Banco.getConnection();

		String sql = "INSERT INTO FUNCIONARIO (NOME, CPF, RUA, NUMERO, BAIRRO, CEP, EMAIL, TELEFONE, CARGO, SALARIO, SERVIÇO)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
		PreparedStatement stmt = Banco.getPreparedStatement(conn, sql, PreparedStatement.RETURN_GENERATED_KEYS);
		ResultSet rs = null;
		try {
			stmt.setString(1, novoFuncionario.getNome());
			stmt.setString(2, novoFuncionario.getCpf());
			stmt.setString(3, novoFuncionario.getRua());
			stmt.setString(4, novoFuncionario.getNumero());
			stmt.setString(5, novoFuncionario.getBairro());
			stmt.setString(6, novoFuncionario.getCep());
			stmt.setString(7, novoFuncionario.getEmail());
			stmt.setNString(8, novoFuncionario.getTelefone());
			stmt.setString(9, novoFuncionario.getCargo());
			stmt.setDouble(10, novoFuncionario.getSalario());
			stmt.setObject(11, novoFuncionario.getServicos());

			rs = stmt.getGeneratedKeys();
			int refIdGerado = 0;

			if (rs.next()) {
				int idGerado = rs.getInt(1);
				refIdGerado = idGerado;

				novoFuncionario.setId(idGerado);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao inserir novo funcionário.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(rs);
			Banco.closePreparedStatement(stmt);
			Banco.closeConnection(conn);
		}

		return novoFuncionario;

	}

	public boolean alterar(Funcionario funcionario) {
		Connection conn = Banco.getConnection();
		String sql = " UPDADE FUNCIONARIO"
				+ "SET NOME =?, CPF = ?, RUA = ?, NUMERO =?, BAIRRO =?, CEP =?,EMAIL =?, TELEFONE =?, CARGO =?, SALARIO =? "
				+ "WHERE ID =?";
		PreparedStatement stmt = Banco.getPreparedStatement(conn, sql, PreparedStatement.RETURN_GENERATED_KEYS);
		int registrosAlterados = 0;
		try {
			stmt.setString(1, funcionario.getNome());
			stmt.setString(2, funcionario.getCpf());
			stmt.setString(3, funcionario.getRua());
			stmt.setString(4, funcionario.getNumero());
			stmt.setString(5, funcionario.getBairro());
			stmt.setString(6, funcionario.getCep());
			stmt.setString(7, funcionario.getEmail());
			stmt.setNString(8, funcionario.getTelefone());
			stmt.setString(9, funcionario.getCargo());
			stmt.setDouble(10, funcionario.getSalario());
			registrosAlterados = stmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("Erro ao atualizar o funcionário.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closePreparedStatement(stmt);
			Banco.closeConnection(conn);
		}

		return registrosAlterados > 0;

	}

	public ArrayList<Funcionario> consultarTodos() {
		Connection conexao = Banco.getConnection();
		String sql = "SELECT * FROM FUNCIONARIO";
		PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);
		ArrayList<Funcionario> funcionarios = new ArrayList<Funcionario>();
		try {
			ResultSet result = stmt.executeQuery();

			while (result.next()) {
				Funcionario funcionario = construirFuncionario(result);
				funcionarios.add(funcionario);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao consultar funcionários.");
			System.out.println("Erro: " + e.getMessage());
		}
		return funcionarios;

	}

	private Funcionario construirFuncionario(ResultSet result) {
		Funcionario func = new Funcionario();
		try {
			func.setId(result.getInt("Id"));
			func.setNome(result.getString("Nome"));
			func.setCpf(result.getNString("CPF"));
			func.setRua(result.getNString("Rua"));
			func.setNumero(result.getNString("Número"));
			func.setCep(result.getNString("CEP"));
			func.setTelefone(result.getString("Telefone"));
			func.setEmail(result.getNString("email"));
			func.setCargo(result.getNString("Cargo"));
			func.setSalario(result.getDouble("Salário"));

		} catch (SQLException e) {
			System.out.println("Erro ao construir funcionário a partir do ResultSet. Causa: " + e.getMessage());
		}
		return func;

	}

	public ArrayList<Funcionario> listarComSeletor(FuncionarioSeletor seletor) {
		String sql = "SELECT * FROM FUNCIONARIO f";
		if (seletor.temFiltro()) {
			sql = criarFiltros(seletor, sql);
		}
		Connection conn = Banco.getConnection();
		PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);

		ArrayList<Funcionario> funcionarios = new ArrayList();
		try {
			ResultSet result = stmt.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return funcionarios;

	}

	private String criarFiltros(FuncionarioSeletor seletor, String sql) {
		sql += "WHERE";
		boolean primeiro = true;

		if (seletor.getIdFuncionario() > 0) {
			if (!primeiro) {
				sql += "AND";
			}
			sql += "funcionario.id" + seletor.getIdFuncionario();
			primeiro = false;
		}

		if ((seletor.getNome() != null) && (seletor.getNome().trim().length() > 0)) {
			if (!primeiro) {
				sql += "AND";
			}

			if ((seletor.getCargo() != null) && (seletor.getCargo().trim().length() > 0)) {
				if (!primeiro) {
					sql += "AND";
				}
				if (seletor.getSalario() > 0) {
					if (!primeiro) {
						sql += "AND";
					}

				}

			}

		}
		return sql;
	}

	public boolean excluir(int id) {
		Connection conn = Banco.getConnection();
		String sql = "DELETE FROM FUNCIONARIO WHERE ID= " + id;
		PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);

		int quantidadeLinhasAfetadas = 0;
		try {
			quantidadeLinhasAfetadas = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("Erro ao excluir funcionário.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closePreparedStatement(stmt);
			Banco.closeConnection(conn);
		}
		boolean excluiu = quantidadeLinhasAfetadas > 0;

		return excluiu;
	}
}