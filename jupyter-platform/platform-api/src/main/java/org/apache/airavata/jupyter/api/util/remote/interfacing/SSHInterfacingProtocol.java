package org.apache.airavata.jupyter.api.util.remote.interfacing;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;
import net.schmizz.sshj.userauth.method.*;
import net.schmizz.sshj.userauth.password.PasswordFinder;
import net.schmizz.sshj.userauth.password.PasswordUtils;
import net.schmizz.sshj.userauth.password.Resource;
import net.schmizz.sshj.xfer.scp.SCPFileTransfer;
import org.apache.airavata.jupyter.api.entity.interfacing.SSHInterfaceEntity;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SSHInterfacingProtocol extends InterfacingProtocol {

    private static final Logger logger = LoggerFactory.getLogger(SSHInterfacingProtocol.class);

    private SSHInterfaceEntity sshInterfaceEntity;
    private SSHClient sshClient;

    public SSHInterfacingProtocol(SSHInterfaceEntity sshInterfaceEntity, String remoteWorkingDir) throws Exception {
        super(remoteWorkingDir);
        this.sshInterfaceEntity = sshInterfaceEntity;
        this.sshClient = createSSHClient();
    }


    private SSHClient createSSHClient() throws Exception {

        try {
            SSHClient sshClient = new SSHClient();
            sshClient.addHostKeyVerifier(new PromiscuousVerifier());
            sshClient.connect(sshInterfaceEntity.getHostName(), sshInterfaceEntity.getPort());
            sshClient.getConnection().getKeepAlive().setKeepAliveInterval(5);


            PasswordFinder passwordFinder = sshInterfaceEntity.getPassphrase() != null ?
                    PasswordUtils.createOneOff(sshInterfaceEntity.getPassphrase().toCharArray()) : null;

            KeyProvider keyProvider = sshClient.loadKeys(sshInterfaceEntity.getPrivateKey(),
                    sshInterfaceEntity.getPublicKey(), passwordFinder);

            final List<AuthMethod> am = new LinkedList<>();
            // am.add(new AbstractAuthMethod("none") {});
            am.add(new AuthPublickey(keyProvider));

            am.add(new AuthKeyboardInteractive(new ChallengeResponseProvider() {
                @Override
                public List<String> getSubmethods() {
                    return new ArrayList<>();
                }

                @Override
                public void init(Resource resource, String name, String instruction) {

                }

                @Override
                public char[] getResponse(String prompt, boolean echo) {
                    return new char[0];
                }

                @Override
                public boolean shouldRetry() {
                    return false;
                }
            }));

            sshClient.auth(sshInterfaceEntity.getUserName(), am);
            return sshClient;
        } catch (Exception e) {
            logger.error("Failed to create ssh connection for host {} on port {} and user {}",
                    sshInterfaceEntity.getHostName(),
                    sshInterfaceEntity.getPort(),
                    sshInterfaceEntity.getUserName());
            throw e;
        }
    }

    @Override
    public boolean createDirectory(String relativePath) throws Exception {
        if (!sshClient.isConnected()) {
            createSSHClient();
        }

        SFTPClient sftpClient = sshClient.newSFTPClient();
        sftpClient.mkdirs(getRemoteWorkingDir() + "/" + relativePath);
        sftpClient.close();
        return true;
    }

    @Override
    public boolean transferFileToRemote(String localPath, String remoteRelativePath) throws Exception {

        if (!sshClient.isConnected()) {
            createSSHClient();
        }

        SCPFileTransfer scpFileTransfer = sshClient.newSCPFileTransfer();
        scpFileTransfer.upload(localPath, getRemoteWorkingDir() + "/" + remoteRelativePath);
        return true;
    }

    @Override
    public boolean transferFileFromRemote(String remoteRelativePath, String localPath) throws Exception {

        if (!sshClient.isConnected()) {
            createSSHClient();
        }

        SCPFileTransfer scpFileTransfer = sshClient.newSCPFileTransfer();
        scpFileTransfer.download(getRemoteWorkingDir() + "/" + remoteRelativePath, localPath);
        return true;
    }

    @Override
    public ExecutionResponse executeCommand(String relativeWorkDir, String command) throws Exception {

        if (!sshClient.isConnected()) {
            createSSHClient();
        }

        Session session = null;
        ExecutionResponse response = null;
        try {
            session = sshClient.startSession();
            final Session.Command execResult = session.exec("cd " + getRemoteWorkingDir() + "/" + relativeWorkDir + "; " + command);
            response = new ExecutionResponse();
            response.setStdOut(readStringFromStream(execResult.getInputStream()));
            response.setStdErr(readStringFromStream(execResult.getErrorStream()));

            execResult.join(5, TimeUnit.SECONDS);
            response.setCode(execResult.getExitStatus());

        } finally {
            if (session != null) {
                session.close();
            }
        }
        return response;
    }


    private String readStringFromStream(InputStream is) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer, "UTF-8");
        return writer.toString();
    }
}
