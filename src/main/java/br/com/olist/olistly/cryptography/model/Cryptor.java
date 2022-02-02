package br.com.olist.olistly.cryptography.model;

import br.com.olist.olistly.cryptography.exception.DecryptionException;
import br.com.olist.olistly.cryptography.exception.EncryptionException;

public interface Cryptor {
	public String encrypt(String text) throws EncryptionException;
	public String decrypt(String text) throws DecryptionException;
}
