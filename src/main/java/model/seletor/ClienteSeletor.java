package model.seletor;

public class ClienteSeletor {
	
	private int idCliente;
	private String nome;
	private String cpf;
	

	private int limite;
	private int pagina;
	
	public ClienteSeletor() {
		this.limite = 0;
		this.pagina = -1;
		
	}


	public boolean temFiltro() {
		if (this.idCliente>0) {
			return true;
		}
		
	if((this.nome!= null) && (this.nome.trim().length() >0)){
		return true;
	}
	
	if((this.cpf!= null) && (this.cpf.trim().length()>0)) {
		return true;
	}
	return false;

}
	
	public boolean temPaginacao() {
		return ((this.limite > 0) && (this.pagina > -1));
	}
	public int getOffset() {
		return (this.limite * (this.pagina - 1));
	}


	public int getIdCliente() {
		return idCliente;
	}


	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getCpf() {
		return cpf;
	}


	public void setCpf(String cpf) {
		this.cpf = cpf;
	}


	public int getLimite() {
		return limite;
	}


	public void setLimite(int limite) {
		this.limite = limite;
	}


	public int getPagina() {
		return pagina;
	}


	public void setPagina(int pagina) {
		this.pagina = pagina;
	}
	
}